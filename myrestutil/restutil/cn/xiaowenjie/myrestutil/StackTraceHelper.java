package cn.xiaowenjie.myrestutil;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李佳明
 * @date 2017.10.14
 *
 * 工具类，用于获取main方法所在的类、包，支持多线程中main方法的获取
 */
@Slf4j
public class StackTraceHelper {
	// 正则匹配包名
	private static final String REGEX = "((\\w+)\\.?)";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

	/**
	 * 获取main方法所在的Class
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
	public static Class<?> getMainClass()
			throws ClassNotFoundException, NoSuchMethodException {
		// 添加对main方法的校验，如果没有抛出NoSuchMethodException，则肯定为main方法所在的类
		Class<?> mainClass = Class.forName(getMainClassName());
		mainClass.getMethod("main", String[].class);

		return mainClass;
	}

	/**
	 * 获取main方法对应的StackTraceElement
	 * @return
	 */
	public static StackTraceElement getMainClassStackTraceElement() {
		StackTraceElement[] mainStackTraceElements = getMainThreadStackTraceElements();
		// main方法总是主线程的堆栈的最后一个元素
		return mainStackTraceElements[mainStackTraceElements.length - 1];
	}

	/**
	 * 获取main类的完全限定类名
	 * @return
	 */
	public static String getMainClassName() {
		return getMainClassStackTraceElement().getClassName();
	}

	/**
	 * 获取主线程的StackTrace堆栈
	 * @return
	 */
	public static StackTraceElement[] getMainThreadStackTraceElements() {
		Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
		Set<Thread> keySet = stacks.keySet();
		Thread mainThread = null;
		for (Thread thread : keySet) {
			// 获取主线程
			if (thread.getName().equals("main")) {
				mainThread = thread;
				break;
			}

		}
		return stacks.get(mainThread);
	}

	/**
	 * 获取main类的包名
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public static String getMainPackage()
			throws NoSuchMethodException, ClassNotFoundException {
		Class<?> mainClass = getMainClass();
		String mainPackageName = mainClass.getPackage().getName();
		log.debug("mainPackageName is {}", mainPackageName);
		return mainPackageName;
	}

	/**
	 * 获取部分包名，当deep<=0或者deep大于包名深度时，返回完整pack值 例子： 
	 * 1.pack="com.ljm.test"，deep=2,结果为com.ljm
	 * 2.pack="com.ljm.test"，deep=0,结果为com.ljm.test
	 * 3.pack="com.ljm.test"，deep=5,结果为com.ljm.test
	 * 4.pack="com.ljm.test"，deep=-1,结果为com.ljm.test
	 * @param pack
	 * @param deep
	 * @return
	 */
	public static String getBasePackage(String pack, int deep) {
		StringBuilder stringBuilder = new StringBuilder();
		Matcher matcher = PATTERN.matcher(pack);
		int degree = 0;
		while (matcher.find()) {
			degree++;
			if (deep <= 0 || degree <= deep) {
				String group = matcher.group(2);
				System.out.println(group);
				stringBuilder.append(group).append(".");
			}
			else {
				break;
			}
		}
		String basePackage = stringBuilder.substring(0, stringBuilder.length() - 1);
		log.debug("basePackage is {}", basePackage);
		return basePackage;
	}

	/**
	 * 获取main类的部分包名
	 * @param deep
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public static String getBasePackageByMain(int deep)
			throws NoSuchMethodException, ClassNotFoundException {
		String mainPackage = getMainPackage();
		return getBasePackage(mainPackage, deep);
	}

	/**
	 * 判断程序是否是由JUnit启动的
	 * @param mainStackTraceElements
	 * @return
	 */
	public static boolean isRunByJunit(StackTraceElement[] mainStackTraceElements) {
		String className = getMainClassName();
		log.info("main stackTrace Element class name is {}", className);
		if (className.toUpperCase().indexOf("JUNIT") != -1) {
			return true;
		}

		return false;
	}

}
