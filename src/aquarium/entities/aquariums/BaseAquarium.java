package aquarium.entities.aquariums;

import aquarium.common.ConstantMessages;
import aquarium.common.ExceptionMessages;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.fish.Fish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseAquarium implements Aquarium{
    private String name;
    private int capacity;
    private Collection<Decoration> decorations;
    private Collection<Fish> fish;

    public BaseAquarium(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        decorations = new ArrayList<>();
        fish = new ArrayList<>();
    }


    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new NullPointerException(ExceptionMessages.AQUARIUM_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDecorations(Collection<Decoration> decorations) {
        this.decorations = decorations;
    }

    public void setFish(Collection<Fish> fish) {
        this.fish = fish;
    }

    @Override
    public int calculateComfort() {
        return decorations.stream().map(Decoration::getComfort).mapToInt(Integer::intValue).sum();
    }



    @Override
    public void addFish(Fish fish) {
        if(capacity <= this.fish.size()){
            throw new IllegalArgumentException(ConstantMessages.NOT_ENOUGH_CAPACITY);
        }
        this.fish.add(fish);
    }

    @Override
    public void removeFish(Fish fish) {
        this.fish.remove(fish);
    }

    @Override
    public void addDecoration(Decoration decoration) {
        this.decorations.add(decoration);
    }

    @Override
    public void feed() {
        fish.stream().forEach(f -> f.eat());
    }

    @Override
    public String getInfo() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s (%s):",name,getClass().getSimpleName()))
                .append(System.lineSeparator());
        result.append(fish.isEmpty()
        ? "Fish: none"
                : String.format("Fish: %s",fish.stream()
        .map(Fish::getName)
        .collect(Collectors.joining(" "))))
                .append(System.lineSeparator());
        result.append(String.format("Decorations: %s",decorations.size()));
        result.append(System.lineSeparator());
        result.append(String.format("Comfort: %s", calculateComfort()));
        return result.toString();
    }

    @Override
    public Collection<Fish> getFish() {
        return this.fish;
    }

    @Override
    public Collection<Decoration> getDecorations() {
        return this.decorations;
    }
}
