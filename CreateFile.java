package RichmondLab.qPCR;

import java.util.*;
import java.io.*;
import java.lang.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class CreateFile
{	
	public static ArrayList<ArrayList<String>> INFOARRAY = new ArrayList<ArrayList<String>>();
	
	public static void main(String[] args)
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		Scanner scanner = new Scanner(System.in);
		boolean notInitialized = true;
		
		while (notInitialized)
		{
			try
			{
				System.out.print("Enter your file path: ");
				String fileLocation = scanner.nextLine();
				FileInputStream file = new FileInputStream(new File(fileLocation));
				workbook = new XSSFWorkbook(file);	
				notInitialized = false;
			}
			catch (Exception e)
			{				
				System.out.println("File not found. Please re-enter file location.");
				System.out.println();
			}
		}
		
		System.out.print("Type your number of tested genes (counting housekeeping gene): ");
		int numOfGenes = scanner.nextInt();
		System.out.print("Type your housekeeping gene here: ");
		String houseKeepingGene = scanner.next();
		System.out.print("Type your number of copies per sample: ");
		int copiesPerSample = scanner.nextInt();
		
		scanner.close();
		
		initializeInfoArray();
		int samplesPerGene = setInfoArray(workbook, houseKeepingGene);
		fixWellArray(samplesPerGene, numOfGenes);
		addNumberOfCopies();
		addAverageHouseKeeping(samplesPerGene, houseKeepingGene, copiesPerSample);
		addFoldChange(samplesPerGene);
		constructSampleTables();
		writeData();
		constructGraphs();
		
		for (int i = 0; i < INFOARRAY.size(); i++)
		{
			System.out.println(INFOARRAY.get(i));
		}
	}
	
	public static void constructGraphs()
	{
		
	}

	public static void writeData()
	{
		
	}
	
	public static void constructSampleTables()
	{
		
	}
	
	public static void addFoldChange(int samplesPerGene)
	{
		System.out.println(INFOARRAY.get(0).size()/samplesPerGene);
		//for (int i = 1; i < )
	}
	
	public static void addAverageHouseKeeping(int samplesPerGene, String houseKeepingGene, int copiesPerSample)
	{
		boolean notFinished = true;
		int i = 0; 
		
		while (notFinished)
		{
			i++;

			if (INFOARRAY.get(2).get(i).equals(houseKeepingGene))
			{
				for(int ii = 0; ii < samplesPerGene/copiesPerSample; ii++)
				{
					double average = 0;
					for (int iii = 0; iii < copiesPerSample; iii++)
					{
						average += Double.parseDouble(INFOARRAY.get(8).get(i + ii + samplesPerGene/copiesPerSample * iii));
					}
					
					average = average/copiesPerSample;
					
					INFOARRAY.get(9).add(String.valueOf(average));
				}
				
				notFinished = false;
			}

		}
	}
	
	public static void addNumberOfCopies()
	{
		int thresholdCq = 40;
		
		for (int i = 1; i < INFOARRAY.get(5).size(); i++)
		{
			INFOARRAY.get(8).add(String.valueOf(Math.pow(10,(Double.parseDouble(INFOARRAY.get(5).get(i)) - thresholdCq)/-3.32)));
		}
	}
	
	public static void initializeInfoArray()
	{
		String[] labelArray = {"Well", "Fluor", "Target", "Content", "Sample", "Cq", "Cq Mean",
				"Cq Std. Dev", "# of Copies", "Housekeeping Average", "Fold Change of Housekeeping"};
		
		for (int i = 0; i < labelArray.length; i++)
		{
			INFOARRAY.add(new ArrayList<String>());
			INFOARRAY.get(i).add(labelArray[i]);
		}
	}
	
	public static void fixWellArray(int samplesPerGene, int numOfGenes)
	{
	}
	
	public static int setInfoArray(XSSFWorkbook results, String houseKeepingGene)
	{
		
		XSSFSheet sheet = results.getSheetAt(0);
		Iterator<Row> itr = sheet.iterator();
		Row row = itr.next();
		int samplesPerGene = 0;
		boolean notFinished = true;
		
		while (notFinished)
		{
			try
			{
				row = itr.next();
			}
			catch (Exception e)
			{
				notFinished = false;
				break;
			}
			
			Iterator<Cell> cellIterator = row.cellIterator();
			
			for (int ii = 0; ii < 8; ii++)
			{
				Cell cell = cellIterator.next();
				CellType type = cell.getCellType(); 
				
				if (type == CellType.NUMERIC)
				{
					INFOARRAY.get(ii).add(String.valueOf(cell.getNumericCellValue()));
				}
				else if (type == CellType.STRING && cell.getStringCellValue() != "")
				{
					INFOARRAY.get(ii).add(cell.getStringCellValue());
					if (cell.getStringCellValue().equals(houseKeepingGene))
					{
						samplesPerGene++;
					}
				}
				else
				{
					try
					{
						cell = cellIterator.next();
					}
					catch (Exception e)
					{
						break;
					}
				}
			}
		}
		
		return samplesPerGene;
	}
}
