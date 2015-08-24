package junitparams.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for annotation handling that simulate annotation inheritance. Users can
 * create "stereotype annotation" {@code X} that is annotated by annotation {@code Y};
 * methods from this class pretend that {@code X} "inherits" characteristics from {@code Y}.
 *
 * @author Boleslav Bobcik
 */
public final class Annotations {

	/**
	 * An annotation provides annotation type {@code T} if any of the following conditions is satisfied:
	 * <ul>
	 * <li>Annotation class is {@code T}</li>
	 * <li>Annotation is annotated by an annotation that provides type {@code T}</li>
	 * </ul>
	 * This method simulates operator {@code instanceof} for annotations, as annotations don't support
	 * inheritance at the language level.
	 *
	 * @param source           tested annotation instance
	 * @param requestedType    base annotation class
	 * @return {@code true}    if (1) class of {@code source} is {@code requestedType}, or (2) if any of
	 *                         annotations (recursively) attached to the definition of {@code source}
	 *                         has {@code requestedType}
	 */
	public static boolean providesType(Annotation source, Class<? extends Annotation> requestedType) {
		if ((null == source) || (null == requestedType)) {
			throw new NullPointerException();
		} else if (requestedType.isInstance(source)) {
			return true;
		}
		final List<Annotation> annotationChain = findAnnotationChain(source, requestedType);
		return !annotationChain.isEmpty();
	}

	/**
	 * Obtains value from annotation that provides a certain annotation type. If any stereotype annotation
	 * defines a method that has identical name and compatible type as the method present in the {@code requestedType},
	 * it will be used instead.
	 * <p>
	 * This method simulates an ability to override parent values in "inherited" annotations.
	 *
	 * @param source           annotation instance
	 * @param requestedType    base annotation class
	 * @param parameterName    name of a method in {@code requestedType}
	 * @return                 value of the first occurrence of the parameter in an annotation in the inheritance chain
	 *                         from {@code source} towards the nearest "parent" annotation of type {@code requestedType}.
	 *                         Only parameters with type compatible with the type as defined in {@code requestedType}
	 *                         are considered, incompatible parameters are ignored.
     * @throws IllegalArgumentException
	 *                         if {@code requestedType} does not contain parameter {@code parameterName} or
	 *                         if there are problems obtaining its value
	 */
	public static Object getParameterValue(Annotation source, Class<? extends Annotation> requestedType, String parameterName) {
		// Make sure that the parameter exists in the requested type definition
		final Class<?> parameterType;
		try {
			final Method baseParamMethod = requestedType.getMethod(parameterName);
			parameterType = baseParamMethod.getReturnType();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Annotation parameter '" + parameterName + "' not in annotation type " + requestedType);
		}
		// Process chain of annotations (annotation with requestedType is the last element)
		final List<Annotation> annotationChain = findAnnotationChain(source, requestedType);
		for (final Annotation annotation : annotationChain) {
			final Class<? extends Annotation> annType = annotation.annotationType();
			try {
				final Method currMethod = annType.getMethod(parameterName);
				final Class<?> returnType = currMethod.getReturnType();
				if (parameterType.isAssignableFrom(returnType)) {
					return currMethod.invoke(annotation);
				}
			} catch (NoSuchMethodException e) {
				// Ignore this annotation and proceed to the next chain level
			} catch (Exception e) {
				throw new IllegalArgumentException("Cannot obtain parameter '" + parameterName + "' from annotation " + annotation, e);
			}
		}
		throw new AssertionError("Annotation parameter '" + parameterName + "' not found");
	}

	private static List<Annotation> findAnnotationChain(Annotation start, Class<? extends Annotation> stopType) {
		final Matcher blacklistMatcher = getBlacklistMatcher();
		// Use non-recursive breadth-first search approach
		final Deque<AnnotationTreeNode> searchCandidates = AnnotationTreeNode.initial(start);
		while (!searchCandidates.isEmpty()) {
			final AnnotationTreeNode node = searchCandidates.removeFirst();
			if (stopType.isInstance(node.annotation)) {
				return node.getChainFromRoot();
			}
			for (final Annotation subAnnotation : node.annotation.annotationType().getAnnotations()) {
				blacklistMatcher.reset(subAnnotation.annotationType().getName());
				if (!blacklistMatcher.find()) {
					final AnnotationTreeNode childNode = new AnnotationTreeNode(subAnnotation, node);
					searchCandidates.addLast(childNode);
				}
			}
		}
		return Collections.emptyList();
	}

	private Annotations() {
		throw new AssertionError();
	}

	private static Matcher getBlacklistMatcher() {
		return DEFAULT_BLACKLIST_PATTERN.matcher("");
	}

	private static final Pattern DEFAULT_BLACKLIST_PATTERN = Pattern.compile("^javax?\\.");

	static final class AnnotationTreeNode {
		final Annotation annotation;
		final AnnotationTreeNode parent;

		AnnotationTreeNode(Annotation ann, AnnotationTreeNode parent) {
			this.annotation = ann;
			this.parent = parent;
		}

		LinkedList<Annotation> getChainFromRoot() {
			final LinkedList<Annotation> chain = new LinkedList<Annotation>();
			AnnotationTreeNode node = this;
			while (null != node) {
				chain.addFirst(node.annotation);
				node = node.parent;
			}
			return chain;
		}

		static Deque<AnnotationTreeNode> initial(Annotation ann) {
			final Deque<AnnotationTreeNode> initialDeque = new LinkedList<AnnotationTreeNode>();
			final AnnotationTreeNode rootNode = new AnnotationTreeNode(ann, null);
			initialDeque.add(rootNode);
			return initialDeque;
		}
	}

}
