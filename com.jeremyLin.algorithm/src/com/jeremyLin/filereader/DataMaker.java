package com.jeremyLin.filereader;

import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.IntPredicate;

import javax.print.attribute.standard.RequestingUserName;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import com.opencsv.*;

public class DataMaker {

	public float[][] getData(){
		
		String csvFileName = "C:\\algorithm\\6143.csv";
		CSVReader csvReader = null;
		float[][] stockData = null;

		try {
			csvReader = new CSVReader(new FileReader(csvFileName));
			int rowSize = csvReader.readAll().size() - 1;
			
			csvReader = new CSVReader(new FileReader(csvFileName));
			
			//move to row2
			int columnSize = csvReader.readNext().length - 1;
			
			stockData = new float[columnSize][rowSize];
			
			String[] row = null;
					
			int j = 0;
			while((row = csvReader.readNext()) != null) {
				
		
				
 				for(int i=1; i<columnSize+1; i++){
					stockData[i-1][j] = Float.parseFloat(row[i]);
				}
				
 				j++;
				
			}
			
			
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			try {
				if(csvReader != null){
				csvReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return stockData;
	}
	
	
	public float[] getClosePrice(int order, float[][] stockData){
		
		int length = stockData[order].length;
		float[] closePrice = new float[length];
		
		for(int i=0; i<length; i++){
			closePrice[i] = stockData[order][i];
		}
		return closePrice;
	}
	
	public float[] getMAData(int maSize, float[] closePrice){
		
		int length = closePrice.length;
		float[] maData = new float[length];
		
		for(int i=0; i<length-5; i++){
			float ma = (closePrice[i]+closePrice[i+1]+closePrice[i+2]+closePrice[i+3]+closePrice[i+4])/maSize;
			maData[i+5] = ma;
		}
		
		return maData;
	}
	
	public float[][] getDistanceFromMA(int[] maSet, int[] orderSet, int closePriceOrder, float[][] stockData){
		
		System.out.println("hi");
		
		int xLength = stockData.length;
		int yLength = stockData[0].length;
		
		
		float[] closePrice = getClosePrice(closePriceOrder, stockData);
		
		int maLength = maSet.length;
		int orderLength = orderSet.length;
		
		int notOrderLength = xLength - orderLength;
		int[] notOrderSet = null;
		if(notOrderLength !=0){
			notOrderSet = new int[notOrderLength];
			int k = 0;
			for(int i=0; i<xLength; i++){
				int temp = 0;
				for(int j=0; j<orderLength; j++){
					if(i == orderSet[j]  ){
					
						temp++;
					}
					System.out.println("1");
				}
				
				if(temp == 0){
					
					notOrderSet[k] = i;
				}
				
			}
		}
		
			
		float[][] sData = new float[xLength+(orderLength-1)*maLength][yLength];
		
		for(int k=0; k<maLength; k++){
			int ma = maSet[k];
			
			float[] maData = getMAData(ma, closePrice);
			
			for(int i=0; i<orderLength; i++){
				int order = orderSet[i];
				
				for(int j=0; j<yLength; j++){
					
					sData[(maLength*orderLength)-(i*k)][j] = (stockData[order][j] - maData[j])/stockData[order][j];
					System.out.println("2");
				}
			}
			
		}
		
		if(notOrderLength != 0){
			for(int i=0; i<notOrderLength; i++){
				for(int j=0; j<yLength; j++){
				
					sData[(maLength*orderLength-1)+i][j] = stockData[notOrderSet[i]][j];
					System.out.println("3");
				}
			
			}
		}
		
		return sData;
		
	}
	
	//將目標往前移一行
	public float[][] makeTarget(float[][] rData){
		
		int length = rData[0].length;
		int targetOrder = rData.length - 1;
		//System.out.println(length+""+targetOrder);
		
		for(int i=0; i<length-1; i++){
			
			rData[targetOrder][i] = (rData[targetOrder][i+1]-rData[targetOrder][i])/rData[targetOrder][i];
		}
		
		rData[targetOrder][length-1] = 0;
		
		return rData;
	}
	
	
	public float[][] cutDataArray(int firstOrder, int lastOrder, float[][] sData){
		int cutSize = lastOrder - firstOrder + 1;
		int xLength = sData.length;
		int yLength = sData[0].length;
		
		float [][] data = new float[xLength][yLength - cutSize];
		
		for(int i=0; i<xLength; i++){
			
			for(int j=0; j<yLength - cutSize; j++){
				
				data[i][j] = sData[i][j+cutSize];
				
			}
		}
		
		return data;
	}

	
	
	
	
	
}
