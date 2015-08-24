package junitparams;

import junitparams.converters.builtin.DateParam;
import junitparams.converters.builtin.FileParam;
import junitparams.converters.builtin.URIParam;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URI;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnitParamsRunner.class)
public class ParamsBuiltinConverterTest {

    @Test
    @Parameters({
            "2015-03-20T14:30:59 , 2015-03-20T14:30:59",
            "1999-12-31T23:59:59 , 1999-12-31T23:59:59"
    })
    public void shouldConvertIsoDate(@DateParam Date d, String refDate) throws Exception {
        assertThat(d).isEqualToIgnoringMillis(refDate);
    }

    @Test
    @Parameters({ "", " ", "\t", "\t\r\n \t\r\n" })
    public void shouldConvertBlankAsNullDate(@DateParam Date d) throws Exception {
        assertThat(d).isNull();
    }

    @Test
    @Parameters({
            "/etc/sys/ , other , /etc/sys/other",
            "/a/b/c , d/e/f , /a/b/c/d/e/f",
            "p/q/r , ../s/t/u , p/q/r/../s/t/u"
    })
    public void shouldConvertFile(@FileParam File parent, String child, @FileParam File fullPath) throws Exception {
        final File referencePath = new File(parent, child);
        assertThat(fullPath).isEqualTo(referencePath);
    }

    @Test
    @Parameters({
            "sftp://127.0.0.1:22/etc/fstab , sftp , 127.0.0.1",
            "http://www.github.com/bbobcik , http , www.github.com"
    })
    public void shouldConvertURI(@URIParam URI uri, String refScheme, String refHost) throws Exception {
        assertThat(uri).isNotNull();
        assertThat(uri.getScheme()).isEqualTo(refScheme);
        assertThat(uri.getHost()).isEqualTo(refHost);
    }

}
