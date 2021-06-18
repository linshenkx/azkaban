package azkaban.jobtype.hiveutils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {

    private static final Log LOG = LogFactory.getLog(Utilities.class);

    public static ClassLoader addToClassPath(ClassLoader cloader, String[] newPaths) throws Exception {
        URLClassLoader loader = (URLClassLoader) cloader;
        List<URL> curPath = Arrays.asList(loader.getURLs());
        ArrayList<URL> newPath = new ArrayList<URL>();

        // get a list with the current classpath components
        for (URL onePath : curPath) {
            newPath.add(onePath);
        }
        curPath = newPath;

        for (String onestr : newPaths) {
            URL oneurl = urlFromPathString(onestr);
            if (oneurl != null && !curPath.contains(oneurl)) {
                curPath.add(oneurl);
            }
        }

        return new URLClassLoader(curPath.toArray(new URL[0]), loader);
    }


    public static URL urlFromPathString(String onestr) {
        URL oneurl = null;
        try {
            if (StringUtils.indexOf(onestr, "file:/") == 0) {
                oneurl = new URL(onestr);
            } else {
                oneurl = new File(onestr).toURL();
            }
        } catch (Exception err) {
            LOG.error("Bad URL " + onestr + ", ignoring path");
        }
        return oneurl;
    }
}
