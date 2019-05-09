package com.wayto.map.utils;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.TextSymbol;

/**
 * @package com.yunwei.map.utils
 * @description 地图符号绘制工具
 * @author yangdu
 * @date 10/03/2017
 * @time 11:50 AM
 * @version V1.0
 **/
public class MapSymbolDrawTool {

    /**
     * 生成圆形Graphic
     * @param center 中心点
     * @param radius 半径
     * @param fillColor 填充色
     * @param boundColor 边界线颜色
     * @param boundLineWidth 边界线宽度
     * @param fillAlpha 填充透明度
     */
    public static Graphic createCircleSymbol(Point center, double radius, int fillColor, int boundColor, float boundLineWidth, int fillAlpha){
        Graphic circleGraphic = null;
        if (center!=null&&radius>0) {
            //绘制圆形
            Polygon polygon = new Polygon();
            getCircle(center, radius, polygon);
            FillSymbol fillSymbol=new SimpleFillSymbol(fillColor);
            fillSymbol.setAlpha(fillAlpha);//设置透明度
            LineSymbol lineSymbol = new SimpleLineSymbol(boundColor, boundLineWidth, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setOutline(lineSymbol);//设置边界线
            circleGraphic = new Graphic(polygon, fillSymbol);
        }
        return circleGraphic;
    }

    /**
     * 绘制直线
     * @param startPt 起点
     * @param endPt 终点
     * @param lineColor 线段颜色
     * @param lineWidth 线宽
     * @param dashLine 是否绘制虚线
     * @return
     */
    public static Graphic createLineSymbol(Point startPt, Point endPt, int lineColor, float lineWidth, boolean dashLine){
        Graphic lineGrapihc = null;
        if (startPt!=null&&endPt!=null) {
            Polyline polyline = new Polyline();
            polyline.startPath(startPt);
            polyline.lineTo(endPt);
            LineSymbol lineSymbol = new SimpleLineSymbol(lineColor, lineWidth, dashLine ? SimpleLineSymbol.STYLE.DASH : SimpleLineSymbol.STYLE.SOLID);
            lineGrapihc = new Graphic(polyline, lineSymbol);
        }
        return lineGrapihc;
    }

    /**
     * 绘制文字
     * @param point 位置
     * @param text 要绘制的文字
     * @param textSize 文字大小
     * @param textColor 文字颜色
     * @return
     */
    public static Graphic createTextSymbol(Point point, String text, int textSize, int textColor){
        Graphic textGraphic = null;
        if (point!=null) {
            TextSymbol textSymbol = new TextSymbol(textSize, text, textColor);
            textSymbol.setFontFamily("DroidSansFallback.ttf");//设置显示中文
            textGraphic = new Graphic(point, textSymbol);
        }
        return textGraphic;
    }

    /**
     * 绘制矩形背景
     * @param centerPt 中心点坐标
     * @param width 矩形宽
     * @param height 矩形高
     * @param fillColor 填充颜色
     * @param boundColor 边界线颜色
     * @param boundWidth 边界线宽
     * @param fillAlpha 填充透明度
     * @param boundAlpha 边界透明度
     * @return
     */
    public static Graphic createRectangleSymbol(Point centerPt, double width, double height, int fillColor, int boundColor, int boundWidth, int fillAlpha, int boundAlpha){
        Graphic rectangleGraphic=null;
        if (centerPt != null) {
            Envelope envelope=new Envelope(centerPt,width,height);
            FillSymbol fillSymbol = new SimpleFillSymbol(fillColor);
            fillSymbol.setAlpha(fillAlpha);
            LineSymbol lineSymbol = new SimpleLineSymbol(boundColor, boundWidth, SimpleLineSymbol.STYLE.SOLID);
            lineSymbol.setAlpha(boundAlpha);
            fillSymbol.setOutline(lineSymbol);
            rectangleGraphic = new Graphic(envelope, fillSymbol);
        }
        return rectangleGraphic;
    }

    /**
     * 生成圆形Polygon
     * @param center 中心点坐标
     * @param radius 半径(m)
     * @param circle 返回的Polygon
     */
    private static void getCircle(Point center, double radius, Polygon circle) {
        if (circle != null) {
            circle.setEmpty();
            Point[] points = getPoints(center, radius);
            circle.startPath(points[0]);
            for (int i = 1; i < points.length; i++)
                circle.lineTo(points[i]);
        }
    }

    /**
     * 根据中心点和半径生成圆的边线点集
     * @param center
     * @param radius
     * @return
     */
    private static Point[] getPoints(Point center, double radius) {
        int num=120;
        Point[] points = new Point[num];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < num; i++) {
            sin = Math.sin(Math.PI * 2 * i / num);
            cos = Math.cos(Math.PI * 2 * i / num);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }
}
