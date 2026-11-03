package org.java.collect;

public record Student(int id, String name, double grade) {

    public Student {
        if(id <= 0){
            throw new IllegalArgumentException("id должен быть положительным!");
        }
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("имя не должно быть пустым!");
        }
        if(!Double.isFinite(grade) || grade < 0 || grade > 10){
            throw new IllegalArgumentException("оценка должна быть от 0 до 10!");
        }
    }
}
