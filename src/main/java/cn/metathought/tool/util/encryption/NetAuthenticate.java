package cn.metathought.tool.util.encryption;

/**
 * ip段检测工具类
 * 
 * @author zbl
 * @date 2015.04.10
 *
 */
public class NetAuthenticate {
	public static boolean securityProcessor(String ipAddress, String ipSection) {
		ipSection = ipSection.trim();
		if (null == ipSection || ipSection.equals("")) {
			return false;
		}
		int idx = ipSection.indexOf('-');
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ipAddress.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		boolean b = ips <= ipt && ipt <= ipe;
		if (b) {
			return true;
		} else {
			return false;
		}
	}
}
