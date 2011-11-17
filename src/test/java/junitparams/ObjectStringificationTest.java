package junitparams;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ObjectStringificationTest {
	
	@Test
	public void stringifyString() throws Exception {
		String obj = "dupa";
		
		assertThat(Utils.stringify(obj, 0), is("[0] dupa"));
	}
	
	@Test
	public void stringifyClassWithToStringAndOneParam() throws Exception {
		ClassWithToString obj = new ClassWithToString("dupa");
		
		assertThat(Utils.stringify(obj, 0), is("[0] dupa"));
	}

	@Test
	public void stringifyClassWithToStringAndmanyParams() throws Exception {
		ClassWithToString obj1 = new ClassWithToString("one");
		ClassWithToString obj2 = new ClassWithToString("two");
		
		assertThat(Utils.stringify(new Object[] {obj1, obj2}, 0), is("[0] one, two"));
	}
	
	@Test
	public void stringifyClassWithToStringInSuperclass() throws Exception {
		ClassWithToString obj = new ClassWithToString("dupa") {			
		};
		
		assertThat(Utils.stringify(obj, 0), is("[0] dupa"));
	}

	private class ClassWithToString {
		private String description;

		public ClassWithToString(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return description;
		}
	}
}
