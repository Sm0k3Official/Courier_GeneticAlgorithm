package Graph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph extends JPanel
{
    private static final Stroke GraphStroke = new BasicStroke(2f);

    private int width = 800;
    private int height = 600;
    private int padding = 25;
    private int labelPadding = 25;
    private int pointWidth = 6;
    private int numberYDivisions = 0;
    private int size = 0;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = Color.BLACK;
    private Color gridColor = new Color(200, 200, 200, 200);
    private String graphTitle = "";
    private double[] dataPoints;
    
    public Graph(double[] dataPoints, int size, String grapTitle)
    {
        this.dataPoints = dataPoints;
        this.size = size;
        this.graphTitle = grapTitle;

        numberYDivisions = GetMaxDataPoint() - GetMinDataPoint();

        this.setPreferredSize(new Dimension(width, height));

        JFrame frame = new JFrame(grapTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void PaintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D)graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (this.size - 1);
        double yScale = ((double) getHeight() - (2 * padding) - labelPadding) / (GetMaxDataPoint() - GetMinDataPoint());

        ArrayList<Point> graphPoints = new ArrayList<>();
        for(int i = 0; i < this.size; i++)
        {
            int x1 = (int)(i * xScale + padding + labelPadding);
            int y1 = (int)((GetMaxDataPoint() - dataPoints[i]) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        graphics2d.setColor(Color.WHITE);
        graphics2d.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - (2 * padding) - labelPadding);
        graphics2d.setColor(Color.BLACK);

        for(int i = 0; i < numberYDivisions; i++)
        {
            if(numberYDivisions == 0)
            {
                numberYDivisions = numberYDivisions + 1;
            }

            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;

            int y0 = getHeight() - ((i * (getHeight() - (2 * padding) - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;

            if(this.size > 0)
            {
                graphics2d.setColor(gridColor);
                graphics2d.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                graphics2d.setColor(Color.BLACK);

                String yLabel = ((int)(GetMinDataPoint() + (GetMaxDataPoint() - (GetMinDataPoint()) * (i * 1.0) / numberYDivisions))) + "   ";
                FontMetrics metrics = graphics2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                
                graphics2d.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }

            graphics2d.drawLine(x0, y0, x1, y1);
        }

        for(int i = 0; i < this.size; i++)
        {
            if(this.size > 1)
            {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (this.size - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;

                if((i % ((int)((this.size / 20.0)) + 1)) == 0)
                {
                    graphics2d.setColor(gridColor);
                    graphics2d.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    graphics2d.setColor(Color.BLACK);

                    String xLabel = (i + 1) + "";

                    FontMetrics metrics = graphics2d.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    graphics2d.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }

                graphics2d.drawLine(x0, y0, x1, y1);
            }
        }

        graphics2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        graphics2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        
        Stroke oldStroke = graphics2d.getStroke();
        graphics2d.setColor(lineColor);
        graphics2d.setStroke(GraphStroke);

        for(int i = 0; i < graphPoints.size() - 1; i++)
        {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;

            graphics2d.drawLine(x1, y1, x2, y2);
        }

        graphics2d.setStroke(oldStroke);
        graphics2d.setColor(pointColor);

        for(int i = 0; i < graphPoints.size(); i++)
        {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;

            int ovalW = pointWidth;
            int ovalH = pointWidth;

            graphics2d.fillOval(x, y, ovalW, ovalH);
        }
    }

    private int GetMinDataPoint()
    {
        int minDataPoint = Integer.MAX_VALUE;
        Integer dpConv = 0;

        for(Double dataPoint : dataPoints)
        {
            dpConv = (int)dataPoint.doubleValue();
            minDataPoint = Math.min(minDataPoint, dpConv);
        }

        return minDataPoint;
    }

    private int GetMaxDataPoint()
    {
        int maxDataPoint = Integer.MIN_VALUE;
        Integer dpConv = 0;

        for(Double dataPoint : dataPoints)
        {
            dpConv = (int)dataPoint.doubleValue() + 1;
            maxDataPoint = Math.max(maxDataPoint, dpConv);
        }

        return maxDataPoint;
    }
}