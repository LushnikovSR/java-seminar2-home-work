import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        Задание
//
//        Дана строка sql-запроса "select * from students where ".
//        Сформируйте часть WHERE этого запроса, используя StringBuilder.
//        Данные для фильтрации приведены ниже в виде json-строки.
//        Если значение null, то параметр не должен попадать в запрос.
//        Параметры для фильтрации: {"name":"Ivanov", "country":"Russia", "city":"Moscow", "age":"null"}
        String sqlRequest = "select * from students where ";
        String jsonString = "{\"name\":\"Ivanov\", \"country\":\"Russia\", \"city\":\"Moscow\", \"age\":\"null\"}";
        System.out.println(addWhereCondition(sqlRequest, jsonString));
        System.out.println("======================================================");


//        Дополнительные задания
//
//        Дана json-строка (можно сохранить в файл и читать из файла)
//        [{"фамилия":"Иванов","оценка":"5","предмет":"Математика"},
//        {"фамилия":"Петрова","оценка":"4","предмет":"Информатика"},
//        {"фамилия":"Краснов","оценка":"5","предмет":"Физика"}]
//        Написать метод(ы), который распарсит json и, используя StringBuilder,
//        создаст строки вида: Студент [фамилия] получил [оценка] по предмету [предмет].
//        Пример вывода:
//        Студент Иванов получил 5 по предмету Математика.
//        Студент Петрова получил 4 по предмету Информатика.
//        Студент Краснов получил 5 по предмету Физика.
//        fileWriter();
        showStudentEvaluations("text.txt");
        System.out.println("======================================================");


//        *Сравнить время выполнения замены символа "а" на "А" любой строки
//        содержащей >1000 символов средствами String и StringBuilder.
        String s = "Алиса аплодировала артистичной, азартной акробатке.";
        compareReplaceMethods(s.repeat(10000));
    }

    public static void compareReplaceMethods(String inString) {
//        Показывает продолжительность работы методов replace средствами String и StringBuilder
        System.out.println("Задание по сравнению методов replace библиотек String и StringBuilder:");
        long start = System.currentTimeMillis();
        String newString = inString.replace('а', 'А');
//        System.out.println(newString);
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder(inString);
        int x = builder.indexOf("а");
        while (x > -1) {
            builder.replace(x, x+1, "А");
            x = builder.indexOf("а");
        }
//        System.out.println(builder);
        System.out.println(System.currentTimeMillis() - start);
    }

    public static String addWhereCondition(String sqlRequest, String jsonString) {
//        Парсит принятую JSON строку, добавляет в условие WHERE SQL запроса; возвращает измененную строку SQL запроса.
        System.out.println("Основное задание:");

//        Удаляем из массива строк элемент с null
        String[] tempArray = jsonString.split(",");
        int count = 0;
        for (String i : tempArray) {
            if (i.contains("null")) {
                count++;
            }
        }
        String[] cutNullArray = new String[tempArray.length - count];
        int numberInclusions = 0;
        for (int i = 0; i < tempArray.length; i++) {
            if (!tempArray[i].contains("null")) {
                cutNullArray[i - numberInclusions] = tempArray[i];
            } else {
                numberInclusions++;
            }
        }
        
//        Очищаем строку от знаков
        String[] signs = {"[", "]", "{", "}", " ", "\""};
        String clearString = Arrays.toString(cutNullArray);
        for (int i = 0; i < signs.length; i++) {
            while (clearString.contains(signs[i])) {
                clearString = clearString.replace(signs[i], "");
            }
        }

//        Преобразуем список json строк в sql строку формата ключ-значение
        String sqlWhere = clearString.replace(":", "=").replace(",", " AND ");
        StringBuilder builder = new StringBuilder(sqlWhere);
        int inclusionIndex = builder.indexOf("=");
        int inclusionAndIndex = builder.indexOf("AND");
        while ((inclusionIndex > -1) & (inclusionAndIndex >= -1)) {
            builder.insert(inclusionIndex + 1, "'");
            if (inclusionAndIndex == -1) inclusionAndIndex = builder.length();
            builder.insert(inclusionAndIndex, "'");
            inclusionIndex = builder.indexOf("=", inclusionIndex + 1);
            inclusionAndIndex = builder.indexOf("AND", inclusionAndIndex + "AND".length());
        }

//        Добавляем строку формата ключ-значение в sql запрос и возвращаем его
        StringBuilder sqlBuilder = new StringBuilder(sqlRequest);
        sqlBuilder.append(builder);
        return sqlBuilder.toString();
    }
    public static void showStudentEvaluations(String file) {
//        Дана json-строка (можно сохранить в файл и читать из файла)
//        [{"фамилия":"Иванов","оценка":"5","предмет":"Математика"},
//        {"фамилия":"Петрова","оценка":"4","предмет":"Информатика"},
//        {"фамилия":"Краснов","оценка":"5","предмет":"Физика"}]
//        Написать метод(ы), который распарсит json и, используя StringBuilder,
//        создаст строки вида: Студент [фамилия] получил [оценка] по предмету [предмет].
//        Пример вывода:
//        Студент Иванов получил 5 по предмету Математика.
//        Студент Петрова получил 4 по предмету Информатика.
//        Студент Краснов получил 5 по предмету Физика.
        System.out.println("Дополнительное задание:");

//        Построчно считывем содержимое файла
        try(FileReader fr= new FileReader(file);
            Scanner scan = new Scanner(fr)) {
            while (scan.hasNextLine()) {
                //        Очищаем строку от знаков
                String[] symbols = {"[", "]", "{", "}", " ", "\""};
                String clearString = scan.nextLine();
                for (String symbol : symbols) {
                    while (clearString.contains(symbol)) {
                        clearString = clearString.replace(symbol, "");
                    }
                }
//                Преобразуем строку в список и вытаскиваем значения из элементов по "ключам"
                String[] studentData = clearString.split(",");
                String surname = studentData[0].replace("фамилия:", "");
                String grade = studentData[1].replace("оценка:", "");
                String subject = studentData[2].replace("предмет:", "");
                System.out.printf("Студент " + surname + " получил " + grade + " по предмету " + subject + ".");
                System.out.println();
            }

            fr.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void fileWriter() {

        try(FileWriter writer = new FileWriter("text.txt", false))
        {
            // запись всей строки
            String text = "[{\"фамилия\":\"Иванов\",\"оценка\":\"5\",\"предмет\":\"Математика\"}," +
                    " {\"фамилия\":\"Петрова\",\"оценка\":\"4\",\"предмет\":\"Информатика\"}," +
                    " {\"фамилия\":\"Краснов\",\"оценка\":\"5\",\"предмет\":\"Физика\"}]";
            writer.write(text);

            writer.close();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public static void showFileReader(String file) throws Exception {

        FileReader fr= new FileReader(file);
        Scanner scan = new Scanner(fr);

        while (scan.hasNextLine()) {
            System.out.println(scan.nextLine());
        }

        fr.close();
    }
}
