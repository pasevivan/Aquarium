package aquarium.core;

import aquarium.common.ConstantMessages;
import aquarium.common.ExceptionMessages;
import aquarium.entities.aquariums.Aquarium;
import aquarium.entities.aquariums.FreshwaterAquarium;
import aquarium.entities.aquariums.SaltwaterAquarium;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.decorations.Ornament;
import aquarium.entities.decorations.Plant;
import aquarium.entities.fish.Fish;
import aquarium.entities.fish.FreshwaterFish;
import aquarium.entities.fish.SaltwaterFish;
import aquarium.repositories.DecorationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller{
    private DecorationRepository decorations;
    private Collection<Aquarium> aquariums;

    public ControllerImpl() {
        this.decorations = new DecorationRepository();
        this.aquariums = new ArrayList<>();
    }

    @Override
    public String addAquarium(String aquariumType, String aquariumName) {
        switch (aquariumType){
            case "FreshwaterAquarium":
                FreshwaterAquarium freshwaterAquarium = new FreshwaterAquarium(aquariumName);
                aquariums.add(freshwaterAquarium);
                break;
            case "SaltwaterAquarium":
                SaltwaterAquarium saltwaterAquarium = new SaltwaterAquarium(aquariumName);
                aquariums.add(saltwaterAquarium);
                break;
            default:
                throw new NullPointerException(ExceptionMessages.INVALID_AQUARIUM_TYPE);
        }

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_AQUARIUM_TYPE, aquariumType);
    }

    @Override
    public String addDecoration(String type) {
        switch (type){
            case"Ornament":
                Ornament ornament = new Ornament();
                decorations.add(ornament);
                break;
            case"Plant":
                Plant plant = new Plant();
                decorations.add(plant);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_DECORATION_TYPE);
        }
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_TYPE,type);
    }

    @Override
    public String insertDecoration(String aquariumName, String decorationType) {
        Aquarium aquarium = aquariums.stream().filter(a->a.getName().equals(aquariumName)).findFirst().orElse(null);
        Decoration decoration = decorations.findByType(decorationType);
        if(decoration==null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_DECORATION_FOUND,decorationType));
        }
        aquarium.addDecoration(decoration);
        decorations.remove(decoration);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_IN_AQUARIUM,decorationType,aquarium.getName());
    }

    @Override
    public String addFish(String aquariumName, String fishType, String fishName, String fishSpecies, double price) {
        switch (fishType){
            case"SaltwaterFish":
                SaltwaterFish saltwaterFish = new SaltwaterFish(fishName,fishSpecies,price);
                Aquarium saltwaterAquarium = aquariums.stream().filter(a->a.getName().equals(aquariumName)).findFirst().orElse(null);
                if(saltwaterAquarium==null || !saltwaterAquarium.getClass().getSimpleName().equals("SaltwaterAquarium")){
                    return ConstantMessages.WATER_NOT_SUITABLE;
                }
                saltwaterAquarium.addFish(saltwaterFish);
                break;
            case"FreshwaterFish":
                FreshwaterFish freshwaterFish = new FreshwaterFish(fishName,fishSpecies,price);
                Aquarium freshwaterAquarium = aquariums.stream().filter(a->a.getName().equals(aquariumName)).findFirst().orElse(null);
                if(freshwaterAquarium==null || !freshwaterAquarium.getClass().getSimpleName().equals("FreshwaterAquarium")){
                    return ConstantMessages.WATER_NOT_SUITABLE;
                }
                freshwaterAquarium.addFish(freshwaterFish);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_FISH_TYPE);
        }
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_FISH_IN_AQUARIUM,fishType,aquariumName);
    }

    @Override
    public String feedFish(String aquariumName) {
        Aquarium aquarium = aquariums.stream().filter(a->a.getName().equals(aquariumName)).findFirst().orElse(null);
        aquarium.feed();
        return String.format(ConstantMessages.FISH_FED,aquarium.getFish().size());
    }

    @Override
    public String calculateValue(String aquariumName) {
        Aquarium aquarium = aquariums.stream().filter(a->a.getName().equals(aquariumName)).findFirst().orElse(null);
        double sum = 0;
        for (Fish fish : aquarium.getFish()) {
            sum += fish.getPrice();
        }

        for (Decoration decoration : aquarium.getDecorations()) {
            sum += decoration.getPrice();
        }
        return String.format(ConstantMessages.VALUE_AQUARIUM,aquariumName,sum);
    }

    @Override
    public String report() {
        return aquariums.stream().map(Aquarium::getInfo).collect(Collectors.joining(System.lineSeparator()));
    }
}
