package org.java.collect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StudentRegistry {

    private final HashMap<Integer, Student> students = new HashMap<>();
    private final TreeMap<Integer, Student> sortedStudents = new TreeMap<>(Comparator.reverseOrder());

    public void add(Student student){
        if(students.containsKey(student.id())){
            throw new IllegalArgumentException("id уже существует");
        }
        students.put(student.id(), student);
        sortedStudents.put(student.id(), student);
    }

    public Student get(int id){
        return students.get(id);
    }

    public List<Student> findStudentsByGradeRange(Map<Integer, Student> students, double minGrade, double maxGrade){
        if(!Double.isFinite(minGrade) || !Double.isFinite(maxGrade)
                || minGrade < 0 || minGrade > 10
                || maxGrade < 0 || maxGrade > 10
                || minGrade > maxGrade){
            throw new IllegalArgumentException("неверный диапазон");
        }

        List<Student> result = new ArrayList<>();
        for(Student student : students.values()){
            if(student.grade() >= minGrade && student.grade() <= maxGrade){
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getTopNStudentsById(int n){
        if(n < 0){
            throw new IllegalArgumentException("n не может быть отрицательным");
        }

        List<Student> result = new ArrayList<>();
        for(Student student : sortedStudents.values()){
            if(result.size() == n){
                break;
            }
            result.add(student);
        }
        return result;
    }
}
