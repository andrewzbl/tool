package cn.metathought.tool.loader;

import cn.metathought.tool.loader.util.ClassModifier;
import cn.metathought.tool.loader.util.HackSystem;
import java.lang.reflect.Method;

/**
 * 
 * <p>
 * Description:JavaClass执行工具
 * </p>
 * 
 * @author zbl
 * @date 2016年7月11日 上午9:24:40
 *
 */
public class JavaClassExecuter {
	/**
	 * 执行外部传过来的代表一个Java类的byte数组
	 * <p>
	 * 将输入泪的byte数组中代表java.lang.System的CONSTANT_Utf8_info常量修改为劫持后的HackSystem类
	 * <p>
	 * 执行方法为该类的static main(String[] args)方法，数据结果为该类向System.out/err输出的信息
	 * 
	 * @param classByte
	 *            代表一个Java类的byte数组
	 * @return 执行结果
	 */
	public static String execute(byte[] classByte) {
		HackSystem.clearBuffer();
		ClassModifier cm = new ClassModifier(classByte);
		byte[] modiBytes = cm.modifyUTF8Constant("java/lang/System",
				"cn/mthought/platform/tool/loader/util/HackSystem");
		HotSwapClassLoader loader = new HotSwapClassLoader();
		Class<?> clazz = loader.loadByte(modiBytes);
		try {
			Method method = clazz.getMethod("main", new Class[] { String[].class });
			method.invoke(null, new String[] { null });
		} catch (Throwable e) {
			e.printStackTrace(HackSystem.out);
		}
		return HackSystem.getBufferString();
	}
}
