package com.jeremyLin.algorithm;

import java.awt.FlowLayout;

public class KNN {

	public KNN(){
		
	
		
	}
	
	public void predict(float[][] data){
		
		
		
	}
	
	/*
	 * data[x1 x2 .... rise of price]
	 * */
	
	public float[][] calculate(float[][] data){
		
		int xLength = data.length;
		int yLength = data[0].length;
		
		float[][] resultData = new float[2][yLength-1];
		
		float[] predictData = new float[xLength-1];
		for(int i=0; i<xLength-1; i++){
			predictData[i] = data[i][yLength-1];
		}
		
		for(int j=0; j<yLength-1; j++){
			
			float[] trainData = new float[xLength];
			
			for(int i=0; i<xLength; i++){
				trainData[i] = data[i][j];
			}
			
			resultData = similarity(trainData, predictData, resultData, j);
			
		}
		
		return resultData;
	}
	
	public float[][] similarity(float[] tData, float[] pData, float[][] rData, int order){
		int length = pData.length;
		float dotSum = 0;
		float crossTSum = 0;
		float crossPSum = 0;
		for(int i=0; i<length; i++){
			
			dotSum = dotSum + (tData[i]*pData[i]);
			crossTSum = crossTSum + (tData[i]*tData[i]);
			crossPSum = crossPSum + (pData[i]*pData[i]);
			
		}
		
		rData[0][order] = (float)(dotSum / (Math.sqrt(crossTSum) * Math.sqrt(crossPSum)));
		rData[1][order] = tData[length];
		
		return rData;
	}
	
	public float[][] bubbleSort(float[][] rData){
		
		int length = rData[0].length;
		
		float temp1 = 0;
		float temp2 = 0;
		
		for(int i=length-1; i>0; i-- ){
			
			for(int j=0; j<i; j++){
				
				if(rData[0][j] < rData[0][j+1]){
					
					temp1 = rData[0][j+1];
					temp2 = rData[1][j+1];
					
					rData[0][j+1] = rData[0][j];
					rData[1][j+1] = rData[1][j];
					
					rData[0][j] = temp1;
					rData[1][j] = temp2;
					
				}
			}
		}
		
		return rData;
		
	}
	
	/*
	 * data = [x1 x2 ... xn] no rise of price
	 * 
	 * 最後一欄 target不需要被normalize
	 * */
	
	public float[][] normalize(float[][] data){
		float[][] normalizedData = new float[data.length][data[0].length];
		int xLength = data.length;
		int yLength = data[1].length;
		
		for(int i=0; i<xLength-1; i++){
			
			float sum = 0;
			
			for(int j=0; j<yLength; j++){
				sum = sum + data[i][j];
			}
			
			float mean = sum/yLength;
			float deviationFromMean = 0;
			
			for(int j=0; j<yLength; j++){
				deviationFromMean = (float) (deviationFromMean + Math.pow(data[i][j] - mean, 2));
			}
			
			double sd = (float) Math.sqrt(deviationFromMean/yLength);
			
			for(int j=0; j<yLength; j++){
				normalizedData[i][j] = (float) ((data[i][j]-mean)/sd);
				
			}
			
		}
		
		for(int j=0; j<yLength; j++){
			normalizedData[xLength-1][j] = data[xLength-1][j];
		}
		
		return normalizedData;
		
	}
	
	public float doKNNPrediction(int knnSize, float[][] stockData){
		
		float[][] knnTest = normalize(stockData);
		float[][] result =calculate(knnTest);
		result = bubbleSort(result);
		
		float sum=0;
		float weight=0;
		
		for(int j=0; j<knnSize; j++){
			
			sum = sum + result[1][j] * (knnSize-j);	
			weight = weight + (j+1);	
		}
		
		float prediction = sum/weight;
		return prediction;
		
	}
	
	/*
	 * [prediction real]
	 * */
	
	public float[][] doKNNVerification(int verSize, int knnSize, float[][] stocData){
		float[][] verData = new float[2][verSize];
		
		int xLength = stocData.length;
		int yLength = stocData[0].length;
		
		for(int k=1; k<=verSize; k++){
			int columnSize = yLength-(verSize-k);
			
			float[][] sData = new float[xLength][columnSize];
			
			for(int i=0; i<xLength; i++){
				for(int j=0; j< columnSize; j++){
					
					sData[i][j]=stocData[i][j];
					
				}
			}
			
			float prediction = doKNNPrediction(knnSize, sData);
			
			verData[0][k-1] = prediction;
			verData[1][k-1] = stocData[xLength-1][columnSize-1];
		}
		
		return verData;
 	}
	
	
}
