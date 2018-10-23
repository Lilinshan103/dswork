package dswork.flow.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dswork.flow.util.SvgUtil;

public class MyLine
{
	public static final String COLOR_LINE = "AACCEE";
	
	private MyNode from = null;
	private MyNode to = null;
	private String color = "";
	private String forks = "";
	private int style = 0;
	private List<MyPoint> points = new ArrayList<MyPoint>();

	public MyNode getFrom()
	{
		return from;
	}

	public void setFrom(MyNode from)
	{
		this.from = from;
	}

	public MyNode getTo()
	{
		return to;
	}

	public void setTo(MyNode to)
	{
		this.to = to;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}
	
	public String getForks()
	{
		return forks;
	}

	public void setForks(String forks)
	{
		this.forks = forks == null ? "" : forks;
	}

	public int getStyle()
	{
		return style;
	}

	public void setStyle(int style)
	{
		this.style = style;
	}

	public String getPoints()
	{
		return pointsToString(points);
	}

	public void setPoints(String points)
	{
		for(String s : points.split(" "))
		{
			String[] ss = s.split(",");
			if(ss.length == 2)
			{
				this.points.add(new MyPoint(Integer.valueOf(ss[0]), Integer.valueOf(ss[1])));
			}
		}
	}

	public List<MyPoint> getPointsList()
	{
		return points;
	}

	public void setPointsList(List<MyPoint> points)
	{
		this.points = points;
	}

	public void addPoints(MyPoint point)
	{
		this.points.add(point);
	}

	public void clearPoints()
	{
		this.points.clear();
	}

	public void toSvg(StringBuilder sb)
	{
		Map<String, List<MyPoint>> p = lineInfo(3);
		sb.append("<g>").append(SvgUtil.getPolyline("#ffffff", 0, "#" + color, 3, pointsToString(p.get("points")))).append(SvgUtil.getPolygon("#" + color, pointsToString(p.get("arrows"))));
		for(int i = 0; i < points.size(); i++)
		{
			points.get(i).toSvg(sb);
		}
		sb.append("</g>"); 
	}

	public void toXml(StringBuilder sb)
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		// map.put("from", from);
		map.put("to", to.getAlias());
		if(!COLOR_LINE.equals(getColor()))
		{
			map.put("color", color);
		}
		String p = pointsToString(points);
		if(!"".equals(p))
		{
			map.put("p", p);
		}
		sb.append(SvgUtil.getTag("line", map, ""));
	}

	/**
	 * 端点list转坐标字符串
	 * @param p
	 * @return
	 */
	private static String pointsToString(List<MyPoint> p)
	{
		if(p.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(p.get(0).x).append(",").append(p.get(0).y);
			for(int i = 1; i < p.size(); i++)
			{
				sb.append(" ").append(p.get(i).x).append(",").append(p.get(i).y);
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 计算线段（point到节点中心）在节点形状（圆形&矩形）上的足点
	 * @param node
	 * @param point
	 * @return
	 */
	private static MyPoint crossPoint(MyNode node, MyPoint point)
	{
		double x, y;
		MyPoint c = node.getCenter();
		if("start".equals(node.getAlias()) || "end".equals(node.getAlias()))
		{
			double r = node.getWidth() / 2f;
			int W = point.x - c.x, H = point.y - c.y;
			double L = Math.sqrt(Math.pow(W, 2) + Math.pow(H, 2));// 三角斜率
			x = Math.ceil(c.x + r * W / L);
			y = Math.ceil(c.y + r * H / L);
		}
		else
		{
			double[] w = new double[3], h = new double[3];
			// 四个边框和中心点，和点进行连线，组成两个三角形
			// 中心和点组成的三角差
			w[0] = point.x - c.x;
			h[0] = point.y - c.y;
			// 中心和边框上或下宽组成的三角差
			w[1] = (w[0] > 0 ? 1 : -1) * (node.getWidth() / 2);
			h[1] = w[0] != 0 ? h[0] / w[0] * w[1] : (node.getHeight() / 2);// h[1]/w[1]=h[0]/w[0]，按三角斜率计算边
			// 中心和边框左或右高组成的三角差
			h[2] = (h[0] > 0 ? 1 : -1) * (node.getHeight() / 2);
			w[2] = h[0] != 0 ? w[0] / h[0] * h[2] : (node.getWidth() / 2);// h[2]/w[2]=h[0]/w[0]，按三角斜率计算边
			// 如果[0]三角在里面，肯定是最小的，所以想交点只在边框上，就只需要取宽或高上的交点即可，即从[1]开始
			double mw = w[1], mh = h[1];
			for(int i = 1; i < 3; i++)
			{
				if(Math.abs(mw) > Math.abs(w[i]))
				{
					mw = w[i];
					mh = h[i];// 取最小的三角
				}
			}
			x = Math.ceil(c.x + mw);
			y = Math.ceil(c.y + mh);
		}
		return new MyPoint((int) x, (int) y);
	}

	/**
	 * 绘制线段（form到to）的箭头
	 * @param from
	 * @param to
	 * @return
	 */
	private static List<MyPoint> arrow(MyPoint start, MyPoint end, int width)
	{
		int aw = 6, ah = 10, h = 8, r = width / 2;
		int[][] p =
		{
				{end.x, end.y},
				{end.x + ah, end.y + aw},
				{end.x + h, end.y + r},
				{end.x + h, end.y},
				{end.x + h, end.y - r},
				{end.x + ah, end.y - aw}
		};
		List<MyPoint> points = new ArrayList<MyPoint>();
		if(start.x == end.x && start.y == end.y)
		{// 当两点重叠时，箭头不用转方向
			for(int i = 0; i < p.length; i++)
			{
				points.add(new MyPoint(p[i][0], p[i][1]));
			}
		}
		else
		{// 使用矩阵进行方向旋转
			double W = start.x - end.x, H = start.y - end.y;
			double L = Math.sqrt(Math.pow(W, 2) + Math.pow(H, 2));
			double c = W / L, s = H / L;
			double[][] matrix =
			{
					{c, -s, (1 - c) * end.x + s * end.y},
					{s, c, (1 - c) * end.y - s * end.x},
					{0, 0, 1}
			};
			for(int i = 0; i < p.length; i++)
			{
				int x = (int) Math.round(matrix[0][0] * p[i][0] + matrix[0][1] * p[i][1] + matrix[0][2]);
				int y = (int) Math.round(matrix[1][0] * p[i][0] + matrix[1][1] * p[i][1] + matrix[1][2]);
				points.add(new MyPoint(x, y));
			}
		}
		return points;
	}

	/**
	 * 计算两个任务节点（如果存在）和线的交点并为线增加交点坐标
	 * @return
	 */
	private Map<String, List<MyPoint>> lineInfo(int strokeWidth)
	{
		MyPoint start = null;
		MyPoint end = null;
		if(points.size() == 0)
		{
			start = crossPoint(from, to.getCenter());
			end = crossPoint(to, from.getCenter());
		}
		else
		{
			start = crossPoint(from, points.get(0));
			end = crossPoint(to, points.get(points.size() - 1));
		}
		List<MyPoint> arr = new ArrayList<MyPoint>();
		arr.add(start);
		for(MyPoint point : points)
		{
			arr.add(point);
		}
		arr.add(end);
		List<MyPoint> rs = new ArrayList<MyPoint>();
		if(arr.size() > 1)
		{
			rs = arrow(arr.get(arr.size() - 2), arr.get(arr.size() - 1), strokeWidth);
		}
		Map<String, List<MyPoint>> map = new HashMap<String, List<MyPoint>>();
		map.put("points", arr);
		map.put("arrows", rs);
		return map;
	}
}
