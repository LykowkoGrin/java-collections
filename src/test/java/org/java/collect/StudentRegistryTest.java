package org.java.collect;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentRegistryTest {

    private final Student student1 = new Student(1, "student1", 0);
    private final Student student2 = new Student(2, "student2", 5);
    private final Student student3 = new Student(3, "student3", 10);
    private final Student student4 = new Student(4, "student4", 7);
    private final List<Student> studentList = List.of(student1, student3, student2);
    private final List<Student> descendingStudents = List.of(student3, student2, student1);
    private final Map<Integer, Student> students = new HashMap<>(Map.of(1, student1, 2, student2, 3, student3));
    private final StudentRegistry registry = new StudentRegistry();
    private final int[] counts = {0, 1, 2, 3, 10};
    private final int[] negativeCounts = {-1, -10};
    private final double[][] invalidRanges = {
            {-1, 5}, {5, 11}, {11, 11}, {-1, -1}, {7, 6},
            {Double.NaN, 5}, {5, Double.NaN},
            {Double.POSITIVE_INFINITY, 10}, {0, Double.POSITIVE_INFINITY},
            {Double.NEGATIVE_INFINITY, 10}, {0, Double.NEGATIVE_INFINITY}
    };

    @Test
    void checkGradeRange(){
        Map<Integer, Student> copy = new HashMap<>(students);
        List<Student> result = registry.findStudentsByGradeRange(students, 5, 10);

        assertEquals(2, result.size());
        assertTrue(result.contains(student2));
        assertTrue(result.contains(student3));
        assertEquals(copy, students);

        List<Student> all = registry.findStudentsByGradeRange(students, 0, 10);
        assertEquals(studentList.size(), all.size());
        assertTrue(all.containsAll(studentList));
        assertEquals(copy, students);
    }

    @Test
    void checkEqualBounds(){
        for(Student student : studentList){
            List<Student> result = registry.findStudentsByGradeRange(
                    students, student.grade(), student.grade());

            assertEquals(1, result.size());
            assertEquals(student, result.get(0));
        }
    }

    @Test
    void checkNoMatches(){
        List<Student> result = registry.findStudentsByGradeRange(students, 6, 9);

        assertTrue(result.isEmpty());
        assertInstanceOf(ArrayList.class, result);
        result.add(student4);
        assertEquals(1, result.size());
        assertEquals(3, students.size());
        assertFalse(students.containsKey(student4.id()));
    }

    @Test
    void checkEmptyMap(){
        Map<Integer, Student> empty = new HashMap<>();
        List<Student> result = registry.findStudentsByGradeRange(empty, 0, 10);

        assertTrue(result.isEmpty());
        assertInstanceOf(ArrayList.class, result);
        assertNotSame(result, registry.findStudentsByGradeRange(empty, 0, 10));
        result.add(student4);
        assertEquals(1, result.size());
        assertTrue(empty.isEmpty());
        empty.put(student1.id(), student1);
        assertEquals(List.of(student4), result);
    }

    @Test
    void checkInvalidRanges(){
        Map<Integer, Student> copy = new HashMap<>(students);

        for(double[] range : invalidRanges){
            assertThrows(IllegalArgumentException.class, () -> registry.findStudentsByGradeRange(students, range[0], range[1]));
        }

        assertEquals(copy, students);
    }

    @Test
    void checkGradeResultIsIndependent(){
        List<Student> result = registry.findStudentsByGradeRange(students, 0, 10);

        assertInstanceOf(ArrayList.class, result);
        assertNotSame(result, registry.findStudentsByGradeRange(students, 0, 10));
        students.put(student4.id(), student4);
        assertEquals(3, result.size());
        assertFalse(result.contains(student4));

        result.clear();
        result.add(student4);
        assertEquals(4, students.size());
        for(Student student : studentList){
            assertEquals(student, students.get(student.id()));
        }
        assertEquals(student4, students.get(student4.id()));
    }

    @Test
    void checkTopStudents(){
        for(Student student : studentList){
            registry.add(student);
        }

        for(int n : counts){
            List<Student> result = registry.getTopNStudentsById(n);
            int expectedSize = Math.min(n, studentList.size());

            assertEquals(expectedSize, result.size());
            for(int i = 0; i < expectedSize; i++){
                assertEquals(descendingStudents.get(i), result.get(i));
            }
            assertInstanceOf(ArrayList.class, result);
            assertNotSame(result, registry.getTopNStudentsById(n));
            result.clear();
            result.add(student4);
            assertEquals(1, result.size());
        }

        assertEquals(descendingStudents, registry.getTopNStudentsById(10));
        for(Student student : studentList){
            assertEquals(student, registry.get(student.id()));
        }
    }

    @Test
    void checkEmptyRegistry(){
        List<Student> result = registry.getTopNStudentsById(5);

        assertTrue(result.isEmpty());
        assertInstanceOf(ArrayList.class, result);
        assertNotSame(result, registry.getTopNStudentsById(5));
        result.add(student4);
        assertTrue(registry.getTopNStudentsById(5).isEmpty());
        assertNull(registry.get(student4.id()));
        registry.add(student1);
        assertEquals(List.of(student4), result);
        assertEquals(List.of(student1), registry.getTopNStudentsById(5));
    }

    @Test
    void checkNegativeCount(){
        for(int n : negativeCounts){
            assertThrows(IllegalArgumentException.class, () -> registry.getTopNStudentsById(n));
        }

        for(Student student : studentList){
            registry.add(student);
        }
        for(int n : negativeCounts){
            assertThrows(IllegalArgumentException.class, () -> registry.getTopNStudentsById(n));
        }

        assertEquals(descendingStudents, registry.getTopNStudentsById(10));
        for(Student student : studentList){
            assertEquals(student, registry.get(student.id()));
        }
    }

    @Test
    void checkTopResultIsIndependent(){
        for(Student student : studentList){
            registry.add(student);
        }
        List<Student> result = registry.getTopNStudentsById(2);

        registry.add(student4);
        assertEquals(List.of(student3, student2), result);
        result.clear();
        result.add(student1);
        assertEquals(List.of(student4, student3, student2, student1), registry.getTopNStudentsById(10));
        for(Student student : studentList){
            assertEquals(student, registry.get(student.id()));
        }
        assertEquals(student4, registry.get(student4.id()));
    }
}
