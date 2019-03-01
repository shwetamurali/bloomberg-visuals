package com.example.shwetaProject;
import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.jfree.chart.ChartUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShwetaProject {

    public static void main(String[] args) throws Exception {
        ArrayList<String> area = new ArrayList<String>();
        ArrayList<String> areaFinal = new ArrayList<String>();
        ArrayList<String> version = new ArrayList<String>();
        ArrayList<String> versionFinal = new ArrayList<String>();
        ArrayList<String> uptime = new ArrayList<String>();
        ArrayList<String> uptimeFinal = new ArrayList<String>();
        ArrayList<String> hostname = new ArrayList<String>();
        ArrayList<String> hostnameFinal = new ArrayList<String>();

        BufferedReader bufferedReader = null;
        JSONParser jsonParser = new JSONParser();
        try {

            String sCurrentLine;

            bufferedReader = new BufferedReader(new FileReader("C:/Users/shwet/Downloads/SBHSData.json"));

            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(sCurrentLine);

                String[]arr=sCurrentLine.split(",");
                for(int i=0;i<arr.length;i++) {
                    if(arr[i].contains("area")) {
                        String[] splitArea = arr[i].split(":");
                        area.add(splitArea[1]);
                    }
                    else if(arr[i].contains("version")) {
                        String[] splitVersion = arr[i].split(":");
                        version.add(splitVersion[1]);
                    }
                    else if(arr[i].contains("uptime")) {
                        String[] splitUp = arr[i].split(":");
                        uptime.add(splitUp[1]);
                    }
                    else {
                        String[] splitHost = arr[i].split(":");
                        hostname.add(splitHost[1]);
                    }
                }

                area.remove(area.size()-1);
                for(int i=0;i<area.size();i++) {
                    areaFinal.add(area.get(i).replaceAll("\"", ""));
                    versionFinal.add(version.get(i).replaceAll("\"", ""));
                    uptimeFinal.add(uptime.get(i).replaceAll("\"", ""));
                    hostnameFinal.add(hostname.get(i).replaceAll("[\"}]" , ""));
                }
                System.out.println("Areas: " + areaFinal);
                System.out.println("Versions: "+ versionFinal);
                System.out.println("upTimes: " +uptimeFinal);
                Collections.sort(hostnameFinal, String.CASE_INSENSITIVE_ORDER);
                System.out.println("hostNames: " +hostnameFinal);


                Object obj;


                try {
                    obj = jsonParser.parse(sCurrentLine);
                    jsonArray.add(obj);


                } catch (ParseException pe) {
                    System.out.println(pe);
                }
            }
        } catch(IOException exception){
            System.out.println(exception);
        }

        //create area bar graph

        final String areaStr = "SBHS Data";

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        Set<String> unique = new HashSet<String>(areaFinal);
        for (String key : unique) {
            dataset.addValue(Collections.frequency(areaFinal, key),areaStr,key );
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Area Type Statistics",
                "Area Type", "Frequency",
                dataset,PlotOrientation.VERTICAL,
                true, true, false);

        int width = 1000;    /* Width of the image */
        int height = 700;   /* Height of the image */
        File BarChart = new File( "C:\\Users\\shwet\\BloombergGraphs\\AreaBarChart.jpeg" );
        ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , height );


        //create version bar graph

        DefaultPieDataset datasetVer = new DefaultPieDataset( );
        Set<String> uniqueVer = new HashSet<String>(versionFinal);
        for (String key : uniqueVer) {
            datasetVer.setValue(key, Collections.frequency(versionFinal, key) );
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Version Types in SBHS Data",   // chart title
                datasetVer,          // data
                true,             // include legend
                true,
                false);

        File pieChart = new File( "C:\\Users\\shwet\\BloombergGraphs\\VersionPieChart.jpeg" );
        ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );

        //create uptime XY graph

        final XYSeries sbhsdata = new XYSeries( "SBHS Data" );
        Set<String> uniqueUp = new HashSet<String>(uptimeFinal);
        for (String key : uniqueUp) {
            if(!key.equals(""))
             sbhsdata.add(Integer.parseInt(key),Collections.frequency(uptimeFinal, key));
        }

        final XYSeriesCollection datasetUp = new XYSeriesCollection( );
        datasetUp.addSeries(sbhsdata);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Uptime Data Statistics",
                "Uptime",
                "Frequency",
                datasetUp,
                PlotOrientation.VERTICAL,
                true, true, false);

        File XYChart = new File( "C:\\Users\\shwet\\BloombergGraphs\\UptimeXYChart.jpeg" );
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);



    }

}