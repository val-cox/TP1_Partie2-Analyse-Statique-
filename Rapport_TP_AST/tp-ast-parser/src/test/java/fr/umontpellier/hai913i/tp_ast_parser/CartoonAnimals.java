package fr.umontpellier.hai913i.tp_ast_parser;

import java.util.ArrayList;
import java.util.List;

abstract class Animal {
    String name;
    int age;

    Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    abstract void speak();
    abstract void move();

    void eat(String food) {
        System.out.println(name + " eats " + food + ".");
    }

    void sleep() {
        System.out.println(name + " is sleeping peacefully.");
    }
}

class Dog extends Animal {
    boolean trained;

    Dog(String name, int age, boolean trained) {
        super(name, age);
        this.trained = trained;
    }

    @Override
    void speak() {
        System.out.println(name + " barks! (Woof woof!)");
    }

    @Override
    void move() {
        System.out.println(name + " runs happily with its tail wagging.");
    }

    void fetch(String item) {
        System.out.println(name + " fetches a " + item + ".");
    }

    void guardHouse() {
        if (trained) {
            System.out.println(name + " guards the house bravely!");
        } else {
            System.out.println(name + " just wags its tail.");
        }
    }
}

class Cat extends Animal {
    int livesLeft = 9;

    Cat(String name, int age) {
        super(name, age);
    }

    @Override
    void speak() {
        System.out.println(name + " meows! (Meow~)");
    }

    @Override
    void move() {
        System.out.println(name + " sneaks gracefully on soft paws.");
    }

    void scratch(String target) {
        System.out.println(name + " scratches " + target + "!");
    }

    void loseLife() {
        if (livesLeft > 0) {
            livesLeft--;
            System.out.println(name + " lost a life! Remaining: " + livesLeft);
        } else {
            System.out.println(name + " is out of lives!");
        }
    }
}

class Bird extends Animal {
    double wingspan;

    Bird(String name, int age, double wingspan) {
        super(name, age);
        this.wingspan = wingspan;
    }

    @Override
    void speak() {
        System.out.println(name + " chirps cheerfully!");
    }

    @Override
    void move() {
        System.out.println(name + " flaps wings and flies high in the sky.");
    }

    void singSong(String tune) {
        System.out.println(name + " sings the tune: " + tune);
    }

    void buildNest() {
        System.out.println(name + " builds a cozy nest.");
    }
}

class Fish extends Animal {
    String waterType; // freshwater or saltwater

    Fish(String name, int age, String waterType) {
        super(name, age);
        this.waterType = waterType;
    }

    @Override
    void speak() {
        System.out.println(name + " makes bubbles... (blub blub)");
    }

    @Override
    void move() {
        System.out.println(name + " swims quickly through the water.");
    }

    void hideInCoral() {
        System.out.println(name + " hides inside the coral reef.");
    }
}

class Zoo {
    List<Animal> animals = new ArrayList<>();

    void addAnimal(Animal a) {
        animals.add(a);
    }

    void showAll() {
        for (Animal a : animals) {
            System.out.println("Meet " + a.name + ", age " + a.age);
            a.speak();
            a.move();
            System.out.println();
        }
    }

    void feedAll(String food) {
        for (Animal a : animals) {
            a.eat(food);
        }
    }

    void countAnimals() {
        System.out.println("The zoo has " + animals.size() + " animals.");
    }
}

public class CartoonAnimals {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();

        zoo.addAnimal(new Dog("Scooby-Doo", 7, true));
        zoo.addAnimal(new Cat("Tom", 5));
        zoo.addAnimal(new Bird("Tweety", 2, 0.3));
        zoo.addAnimal(new Fish("Nemo", 1, "saltwater"));

        zoo.countAnimals();
        zoo.showAll();
        zoo.feedAll("delicious food");

        System.out.println("Special actions:");
        ((Dog) zoo.animals.get(0)).fetch("ball");
        ((Cat) zoo.animals.get(1)).scratch("couch");
        ((Bird) zoo.animals.get(2)).singSong("La-la-la!");
        ((Fish) zoo.animals.get(3)).hideInCoral();
    }
}
