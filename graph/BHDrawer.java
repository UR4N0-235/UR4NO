/**
 * Robocode Graphics Drawer
 *
 * This class provide a graphical interface to robocode internal drawer. You can
 * draw anything from simple dot to a modern ellipse.
 *
 * CREDIT: David Alves and his DrawingBot is father of this class. But it is
 * heavily modify by Nat.
 *
 * Currently support: Dot, Line, Circle, Ellipse, Arc, Rectangle and Text.
 *
 * @author Nat, David Alves
 */

 package ur4n0.graph;

 import robocode.util.Utils;

 import java.awt.Color;
 import java.awt.Graphics2D;
 import java.awt.geom.Line2D;
 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.util.ArrayList;
 import java.util.List;



public class BHDrawer {
	/**
	 * Store all pending renders for this turn.
	 */
	public static List<Renderable> renderables;

	/**
	 * Initialize a variable.
	 */
	public BHDrawer() {
		renderables = new ArrayList<Renderable>();
	}

	/**
	 * Paint pending renders to screen.
	 *
	 * @param g
	 *            Context to paint to.
	 */
	public void onPaint(Graphics2D g) {
		for (Renderable r : renderables)
			r.render(g);
		renderables.clear();
	}

	public void drawDot(Point2D center) {
		renderables.add(new Renderable.Dot(center));
	}

	public void drawDot(Point2D center, Color color) {
		renderables.add(new Renderable.Dot(center, color));
	}

	public void drawCircle(Point2D center, double radius) {
		renderables.add(new Renderable.Circle(center, radius));
	}

	public void drawCircle(Point2D center, double radius, Color color) {
		renderables.add(new Renderable.Circle(center, radius, color));
	}

	public void fillCircle(Point2D center, double radius) {
		renderables.add(new Renderable.Circle(center, radius, true));
	}

	public void fillCircle(Point2D center, double radius, Color color) {
		renderables.add(new Renderable.Circle(center, radius, true, color));
	}

	public void drawEllipse(Point2D center, double width, double height) {
		renderables.add(new Renderable.Ellipse(center, width, height));
	}

	public void drawEllipse(Point2D center, double width, double height,
			Color color) {
		renderables.add(new Renderable.Ellipse(center, width, height, color));
	}

	public void fillEllipse(Point2D center, double width, double height) {
		renderables.add(new Renderable.Ellipse(center, width, height, true));
	}

	public void fillEllipse(Point2D center, double width, double height,
			Color color) {
		renderables.add(new Renderable.Ellipse(center, width, height, true,
				color));
	}

	public void drawArc(Point2D center, double startAngle, double arcAngle,
			double radius) {
		renderables
				.add(new Renderable.Arc(center, startAngle, arcAngle, radius));
	}

	public void drawArc(Point2D center, double startAngle, double arcAngle,
			double radius, Color color) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle,
				radius, color));
	}

	public void drawArc(Point2D center, double startAngle, double arcAngle,
			double width, double height) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle, width,
				height));
	}

	public void drawArc(Point2D center, double startAngle, double arcAngle,
			double width, double height, Color color) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle, width,
				height, color));
	}

	public void fillArc(Point2D center, double startAngle, double arcAngle,
			double radius) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle,
				radius, true));
	}

	public void fillArc(Point2D center, double startAngle, double arcAngle,
			double radius, Color color) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle,
				radius, true, color));
	}

	public void fillArc(Point2D center, double startAngle, double arcAngle,
			double width, double height) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle, width,
				height, true));
	}

	public void fillArc(Point2D center, double startAngle, double arcAngle,
			double width, double height, Color color) {
		renderables.add(new Renderable.Arc(center, startAngle, arcAngle, width,
				height, true, color));
	}

	public void drawRectangle(Rectangle2D rect) {
		renderables.add(new Renderable.Rectangle(rect));
	}

	public void drawRoundRectangle(Rectangle2D rect) {
		renderables.add(new Renderable.Rectangle(rect, false, true));
	}

	public void drawRectangle(Rectangle2D rect, Color color) {
		renderables.add(new Renderable.Rectangle(rect, color));
	}

	public void drawRoundRectangle(Rectangle2D rect, Color color) {
		renderables.add(new Renderable.Rectangle(rect, false, true, color));
	}

	public void fillRectangle(Rectangle2D rect) {
		renderables.add(new Renderable.Rectangle(rect, true));
	}

	public void fillRoundRectangle(Rectangle2D rect) {
		renderables.add(new Renderable.Rectangle(rect, true, true));
	}

	public void fillRectangle(Rectangle2D rect, Color color) {
		renderables.add(new Renderable.Rectangle(rect, true, color));
	}

	public void fillRoundRectangle(Rectangle2D rect, Color color) {
		renderables.add(new Renderable.Rectangle(rect, true, true, color));
	}

	public void drawLine(Line2D line) {
		renderables.add(new Renderable.Line(line));
	}

	public void drawLine(Line2D line, Color color) {
		renderables.add(new Renderable.Line(line, color));
	}

	public void drawLine(Point2D p1, Point2D p2) {
		renderables.add(new Renderable.Line(p1, p2));
	}

	public void drawLine(Point2D p1, Point2D p2, Color color) {
		renderables.add(new Renderable.Line(p1, p2, color));
	}

	public void drawText(String text, Point2D loc) {
		renderables.add(new Renderable.Text(text, loc));
	}

	public void drawText(String text, Point2D loc, Color color) {
		renderables.add(new Renderable.Text(text, loc, color));
	}

	public void drawText(String text, double x, double y) {
		renderables.add(new Renderable.Text(text, x, y));
	}

	public void drawText(String text, double x, double y, Color color) {
		renderables.add(new Renderable.Text(text, x, y, color));
	}

	/**
	 * Abstract class Renderable, superclass for all render.
	 */
	private static abstract class Renderable {
		/**
		 * Render the graphic.
		 *
		 * @param g
		 *            Context to render to.
		 */
		public abstract void render(Graphics2D g);

		/**
		 * Default constant
		 */
		public static final Color DEFAULT_COLOR = Color.white;
		public static final boolean DEFAULT_FILLED = false;

		/**
		 * For render Ellipse object (including Circle and Dot)
		 */
		private static class Ellipse extends Renderable {
			private Point2D center;
			private double width, height;
			private Color color;
			private boolean filled;

			public Ellipse(Point2D center, double radius) {
				this(center, radius * 2, radius * 2);
			}

			public Ellipse(Point2D center, double radius, Color color) {
				this(center, radius * 2, radius * 2, DEFAULT_FILLED, color);
			}

			public Ellipse(Point2D center, double radius, boolean filled) {
				this(center, radius * 2, radius * 2, filled, DEFAULT_COLOR);
			}

			public Ellipse(Point2D center, double radius, boolean filled,
					Color color) {
				this(center, radius * 2, radius * 2, filled, color);
			}

			public Ellipse(Point2D center, double width, double height) {
				this(center, width, height, DEFAULT_FILLED, DEFAULT_COLOR);
			}

			public Ellipse(Point2D center, double width, double height,
					boolean filled) {
				this(center, width, height, filled, DEFAULT_COLOR);
			}

			public Ellipse(Point2D center, double width, double height,
					Color color) {
				this(center, width, height, DEFAULT_FILLED, color);
			}

			public Ellipse(Point2D center, double width, double height,
					boolean filled, Color color) {
				this.center = center;
				this.width = width;
				this.height = height;
				this.color = color;
				this.filled = filled;
			}

			@Override
			public void render(Graphics2D g) {
				g.setColor(color);
				int x, y, w, h;
				x = (int) Math.round(center.getX() - width / 2);
				y = (int) Math.round(center.getY() - height / 2);
				w = (int) Math.round(width);
				h = (int) Math.round(height);
				if (filled)
					g.fillOval(x, y, w, h);
				else
					g.drawOval(x, y, w, h);
			}
		}

		/**
		 * Circle is one of Ellipse object, but only define explicit
		 * constructors for circle only!
		 */
		private static class Circle extends Ellipse {
			public Circle(Point2D center, double radius) {
				super(center, radius);
			}

			public Circle(Point2D center, double radius, Color color) {
				super(center, radius, color);
			}

			public Circle(Point2D center, double radius, boolean filled) {
				super(center, radius, filled);
			}

			public Circle(Point2D center, double radius, boolean filled,
					Color color) {
				super(center, radius, filled, color);
			}
		}

		/**
		 * Dot is another one of Ellipse object, but only define explicit
		 * constructors for dot only!
		 */
		private static class Dot extends Circle {
			public Dot(Point2D point, Color color) {
				super(point, 2, true, color);
			}

			public Dot(Point2D point) {
				super(point, 2, true);
			}
		}

		/**
		 * Arc class render arc. The angle is automatically convert from
		 * robocode's to java's.
		 */
		private static class Arc extends Renderable {
			private boolean filled;
			private double startAngle, arcAngle, width, height;
			private Point2D center;
			private Color color;

			public Arc(Point2D center, double startAngle, double arcAngle,
					double radius) {
				this(center, startAngle, arcAngle, radius * 2, radius * 2);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double radius, boolean filled) {
				this(center, startAngle, arcAngle, radius * 2, radius * 2,
						filled);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double radius, Color color) {
				this(center, startAngle, arcAngle, radius * 2, radius * 2,
						color);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double radius, boolean filled, Color color) {
				this(center, startAngle, arcAngle, radius * 2, radius * 2,
						filled, color);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double width, double height) {
				this(center, startAngle, arcAngle, width, height,
						DEFAULT_FILLED, DEFAULT_COLOR);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double width, double height, boolean filled) {
				this(center, startAngle, arcAngle, width, height, filled,
						DEFAULT_COLOR);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double width, double height, Color color) {
				this(center, startAngle, arcAngle, width, height,
						DEFAULT_FILLED, color);
			}

			public Arc(Point2D center, double startAngle, double arcAngle,
					double width, double height, boolean filled, Color color) {
				this.center = center;
				this.startAngle = startAngle;
				this.arcAngle = arcAngle;
				this.width = width;
				this.height = height;
				this.filled = filled;
				this.color = color;
			}

			@Override
			public void render(Graphics2D g) {
				int x, y, w, h, sa, a;
				x = (int) Math.round(center.getX() - width / 2);
				y = (int) Math.round(center.getY() - height / 2);
				w = (int) Math.round(width);
				h = (int) Math.round(height);

				// Convert from robocode angle to java angle
				// Only needed if using robocoder prior to 1.6.1.3
				//sa = (int) Math.round(Math.toDegrees(Utils
				//		.normalAbsoluteAngle(-startAngle + Math.PI / 2)));
				// Otherwise use:
				sa = (int) Math.round(Math.toDegrees(Utils.normalAbsoluteAngle(startAngle)));

				a = (int) Math.round(arcAngle);

				g.setColor(color);

				if (filled)
					g.fillArc(x, y, w, h, sa, a);
				else
					g.drawArc(x, y, w, h, sa, a);
			}

		}

		/**
		 * Rectangle class render Rectangle both sharp and rounded.
		 */
		private static class Rectangle extends Renderable {
			private static final boolean DEFAULT_ROUNDED = false;

			private Rectangle2D rect;
			private Color color;
			private boolean filled, rounded;

			public Rectangle(Rectangle2D rect) {
				this(rect, DEFAULT_FILLED);
			}

			public Rectangle(Rectangle2D rect, boolean filled) {
				this(rect, filled, DEFAULT_COLOR);
			}

			public Rectangle(Rectangle2D rect, Color color) {
				this(rect, DEFAULT_FILLED, DEFAULT_ROUNDED, color);
			}

			public Rectangle(Rectangle2D rect, boolean filled, Color color) {
				this(rect, filled, DEFAULT_ROUNDED, color);
			}

			public Rectangle(Rectangle2D rect, boolean filled, boolean rounded) {
				this(rect, filled, rounded, DEFAULT_COLOR);
			}

			public Rectangle(Rectangle2D rect, boolean filled, boolean rounded,
					Color color) {
				this.rect = rect;
				this.filled = filled;
				this.rounded = rounded;
				this.color = color;
			}

			@Override
			public void render(Graphics2D g) {
				int x, y, w, h, arc;
				x = (int) Math.round(rect.getX());
				y = (int) Math.round(rect.getY());
				w = (int) Math.round(rect.getWidth());
				h = (int) Math.round(rect.getHeight());

				arc = (int) Math.round(Math.min(15, Math.min(w / 5, h / 5)));

				g.setColor(color);
				if (rounded) {
					if (filled)
						g.fillRoundRect(x, y, w, h, arc, arc);
					else
						g.drawRoundRect(x, y, w, h, arc, arc);
				} else {
					if (filled)
						g.fillRect(x, y, w, h);
					else
						g.drawRect(x, y, w, h);
				}
			}

		}

		/**
		 * Line class render line.
		 */
		private static class Line extends Renderable {
			private Color color;
			private Line2D line;

			public Line(Point2D p1, Point2D p2) {
				this(new Line2D.Double(p1, p2));
			}

			public Line(Line2D line) {
				this(line, DEFAULT_COLOR);
			}

			public Line(Point2D p1, Point2D p2, Color color) {
				this(new Line2D.Double(p1, p2), color);
			}

			public Line(Line2D line, Color color) {
				this.line = line;
				this.color = color;
			}

			public void render(Graphics2D g) {
				g.setColor(color);
				g.drawLine((int) Math.round(line.getX1()), (int) Math
						.round(line.getY1()), (int) Math.round(line.getX2()),
						(int) Math.round(line.getY2()));
			}
		}

		/**
		 * Text class render String or text.
		 */
		private static class Text extends Renderable {
			private String text;
			private double x, y;
			private Color color;

			public Text(String text, double x, double y) {
				this(text, x, y, DEFAULT_COLOR);
			}

			public Text(String text, double x, double y, Color color) {
				this(text, new Point2D.Double(x, y), color);
			}

			public Text(String text, Point2D loc) {
				this(text, loc, DEFAULT_COLOR);
			}

			public Text(String text, Point2D loc, Color color) {
				this.text = text;
				this.x = loc.getX();
				this.y = loc.getY();
				this.color = color;
			}

			public void render(Graphics2D g) {
				g.setColor(color);
				g.drawString(text, (float) x, (float) y);
			}
		}
	}
}
