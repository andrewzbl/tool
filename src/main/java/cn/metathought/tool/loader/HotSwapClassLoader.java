package cn.metathought.tool.loader;

/**
 * 
 * <p>
 * Description:为了多次载入执行类而加入的加载器
 * </p>
 * 
 * @author zbl
 * @date 2016年7月11日 上午8:18:36
 *
 */
public class HotSwapClassLoader extends ClassLoader {

	public HotSwapClassLoader() {
		super(HotSwapClassLoader.class.getClassLoader());
	}

	/**
	 * 把defineClass公开出来，只有外部显示调用的时候才会使用到loadByte方法，由虚拟机调用时，仍然按照原有的双亲委派规则使用loadClass方法进行加载
	 * 
	 * @param classByte
	 * @return
	 */
	public Class<?> loadByte(byte[] classByte) {
		return defineClass(null, classByte, 0, classByte.length);
	}
}
