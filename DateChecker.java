//Declaring libraries
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateChecker{
	
	public static ArrayList<ArrayList<String>> readFileContents(String path) {
		/** Taking path of a file and return the contents of each row into 2-D Array List
		 * Input: String of the file path
		 * Output: 2-D Array List (Each row in the array correspond to each row from the file)
		 */
		ArrayList<ArrayList<String>> employees = new ArrayList<ArrayList<String>>();
		BufferedReader bufferReader = null;
		try
    	{
			String lineString;
			int i = 0;
			File pathFile = new File(path); 
			//Creating buffer to read the file
			bufferReader = new BufferedReader(new FileReader(pathFile));
			
			//Until the whole file is not read the while loop continues
			while ((lineString = bufferReader.readLine()) != null) {
				// String array[] = lineString.split(","); //Splitting the words from each row into array
				String array[] = lineString.trim().split(",");
				for (int j = 0; j < array.length; j++)
					array[j] = array[j].trim();
				ArrayList<String> employee = new ArrayList<String>(Arrays.asList(array)); //Appending the split array into the array list
				employees.add(employee);
				i+= 1;
			}
			//Closing the buffer
			bufferReader.close();
			return employees;
		} catch (FileNotFoundException ex) {
				System.out.println("Text file cannot be found!");
		} catch (IOException e){
				System.out.println(e);
		} finally {
			//Closing the file in case of error to prevent memory leaks
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}

		return employees;
	}

	public static Date dateConvertCheck(String date1, String date2, Boolean startDate){
		/** Takes two dates and returns the older date if the startDate is true or newer date if opposite 
		 * Input: Takes two dates of type string and boolean
		 * Output: A single date based on the boolean
		*/

		Date currentDate = new Date();
		Date sharedDate = new Date();
		
		try {
			SimpleDateFormat stringDate = new SimpleDateFormat("yyyy-MM-dd");

			//Checking for null value
			if (date1.equals("NULL")){
				date1 = stringDate.format(currentDate);
			}
			if (date2.equals("NULL")) {
				date2 = stringDate.format(currentDate);
			}
			Date convertedDateEmployee1 = stringDate.parse(date1);
			Date convertedDateEmployee2 = stringDate.parse(date2);
			int comparedDate = convertedDateEmployee1.compareTo(convertedDateEmployee2);


			if (comparedDate == -1 && startDate){
				sharedDate = convertedDateEmployee2;
			} else if (comparedDate == 1 && startDate){
				sharedDate = convertedDateEmployee1;
			} else if (comparedDate == -1 && !startDate){
				sharedDate = convertedDateEmployee1;
			}else if (comparedDate == 1 && !startDate){
				sharedDate = convertedDateEmployee2;
			}else {
				sharedDate = convertedDateEmployee1;
			}

		} catch (ParseException e) {
			System.out.println(e);
	}
		return sharedDate;
	}

	public static int[] getMaxValue(int[][] numbers) {
		/** Return the max value from 2-Dimentional array
		 * Input: 2-D array
		 * Output: Array (The max value with the positon indices of that value)
		 */
		int [] result = new int [3];
		int maxValue = numbers[0][0];
		
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                if (numbers[i][j] > maxValue) {
					result[0] = numbers[i][j];
					result[1] = i;
					result[2] = j;
                }
            }
        }
        return result;
	}
	public static void main(String[] args) {
		try {
		ArrayList<ArrayList<String>> employees = new ArrayList<ArrayList<String>>();

		Date sharedStartDate;
		Date sharedEndDate;
		
		if (args.length < 1)
			throw new IllegalArgumentException("Path to the file is required!");
		employees = readFileContents(args[0]);
		
		int [][] table = new int[employees.size()][employees.size()];;
		
		//Iterating over the Array List containing the employees data ( O(n^2) complexity)
		for(int i = 0; i < employees.size(); i++){
			String employee1 = (String) employees.get(i).get(0);
			
			for (int j = 0; j < employees.size(); j++){
				
				String employee2 = (String) employees.get(j).get(0);
				if (employee1 != employee2) {
					if(employees.get(i).get(1).equals(employees.get(j).get(1))){
						//Taking the dates from the employees who worked on the same project
						String startDateEmployee1 = employees.get(i).get(2);
						String startDateEmployee2 = employees.get(j).get(2);
						String endDateEmployee1 = employees.get(i).get(3);
						String endDateEmployee2 = employees.get(j).get(3);
						
						//Calcutating the date in which both employess have worked together
						sharedStartDate = dateConvertCheck(startDateEmployee1, startDateEmployee2, true);
						sharedEndDate = dateConvertCheck(endDateEmployee1, endDateEmployee2, false);
						
						//Calculating the days between the dates
						long sharedDays = sharedEndDate.getTime() - sharedStartDate.getTime();
						table[i][j] = (int) TimeUnit.DAYS.convert(sharedDays, TimeUnit.MILLISECONDS);
					}
				}
			}
		}
		//Retrieving the max value from the 2-D Array List
		int [] result = getMaxValue(table);
		System.out.println("Employees ID: " 
		+ employees.get(result[1]).get(0) 
		+ " and " 
		+ employees.get(result[2]).get(0) 
		+ " have worked on project " 
		+  employees.get(result[1]).get(1)
		+ " for "
		+ result[0]
		+ " days!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}