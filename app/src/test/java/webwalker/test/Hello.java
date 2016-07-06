/**
 * 
 */
package webwalker.test;

import java.io.File;

/**
 * @author xu.jian
 * 
 */
public class Hello {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getUserAgent();

		/*
		sb = new StringBuilder();
		String path = "E:\\Projects\\libs\\Jars";
		getPath(path);
		System.out.println(sb.toString());
		*/
	}

	public static String getUserAgent() {
		String formatVerison = "01";
		String os = String.format("%-7s", "Android");
		String osVersion = String.format("%-6s",
				android.os.Build.VERSION.RELEASE);
		String deviceToken = String.format("%-64s", "fffffffffffffffffffff");
		String versionName = String.format("%-6s", "1.0.1");
		String channel = String.format("%-16s", "myApp");
		String ua = (formatVerison + os + osVersion + deviceToken + versionName + channel)
				.replace(" ", "=");

		System.out.println(ua);
		return ua;
	}

	static StringBuilder sb = new StringBuilder();
	static String format = "<pathelement location=\"../jars/{0}\" />";

	public static void getPath(String path) {
		File file = new File(path);
		File files[] = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				getPath(f.getPath());
				continue;
			}
			String s = format.replace("{0}", f.getName());
			sb.append(s).append("\n");
		}
	}
}
