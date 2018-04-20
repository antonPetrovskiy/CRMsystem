package com.example.tosch.controldevelopers;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileWorker {
    private File statisticFile;
    private File NamesFile;
    private File viewsFile;
    private File profile;
    private File rank;
    File sdPath;

    public FileWorker(){
        createFile();
    }

    public void createFile(){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }

        sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "ControlDevelopers");
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        statisticFile = new File(sdPath, "statistic");
        NamesFile = new File(sdPath, "namesList");
        profile = new File(sdPath, "profile");
        viewsFile = new File(sdPath, "views");
        rank = new File(sdPath, "rank");



    }

    public void writeMac(String s){
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(NamesFile, true));
            bw.write(s  + "\n");

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String s){
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(statisticFile, true));
            // пишем данные
            Date dateNow;

            dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");
            bw.write(formatForDateNow.format(dateNow) + " " + s  + "\n");


            // закрываем поток
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getStatus(){
        String lastLine = "";
        String sCurrentLine = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(statisticFile));
            lastLine = "";
            sCurrentLine = "";

            while ((sCurrentLine = br.readLine()) != null)
            {
                System.out.println(sCurrentLine);
                lastLine = sCurrentLine;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (lastLine.charAt(lastLine.length()-1) == 'n');

    }

    public ArrayList<String> getFileArray(File f){
        String sCurrentLine = "";
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            sCurrentLine = "";
            while ((sCurrentLine = br.readLine()) != null)
            {
                list.add(sCurrentLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void removeText(String s, File f){
        ArrayList<String> list;
        ArrayList<String> newList = new ArrayList<>();
        list = getFileArray(getMACFile());
        for (int i = 0; i < list.size(); i++){
            if(!list.get(i).contains(s)){
                newList.add(list.get(i));
            }
        }

        removeFile(getMACFile());
        NamesFile = new File(sdPath, "MAC");

        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(NamesFile, true));
            for (int i = 0; i < newList.size(); i++){
                bw.write(newList.get(i)  + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFile(File f){
        f.delete();
    }

    public void updateFile(String s){
        removeFile(getMACFile());
        NamesFile = new File(sdPath, "namesList");


        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(NamesFile, true));
            for (String str : s.split("=")) {
                bw.write(str  + "\n");
            }


            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeViews(boolean b){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(viewsFile));
            if(b){
                bw.write("true");
            }else{
                bw.write("false");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean readViews(){
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(statisticFile));
            line = br.readLine();
            if(line.contains("true")){
                br.close();
                return true;
            }else{
                br.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void writeRank(String s){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rank));
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readRank(){
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(rank));
            line = br.readLine();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeName(String s){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(profile));
            if(s!=null) {
                bw.write(s);
            }else{
                bw.write("user");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readName(){
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(profile));
            line = br.readLine();
            if(!line.equals("")){
                return line;
            }else{
                return "userName";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getMACFile(){
        return NamesFile;
    }

}
