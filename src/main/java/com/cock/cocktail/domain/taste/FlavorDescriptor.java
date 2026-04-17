package com.cock.cocktail.domain.taste;

import java.util.function.Function;

public enum FlavorDescriptor {
    ABV          (FlavorVector::getAbv,           tasteProfileWith().body(0.1).abv(1.0).build()),
    CARBONATED   (FlavorVector::getCarbonated,    tasteProfileWith().body(0.3).sour(0.1).build()),
    SWEETNESS    (FlavorVector::getSweetness,     tasteProfileWith().sweet(0.9).body(0.3).build()),
    SOURNESS     (FlavorVector::getSourness,      tasteProfileWith().body(0.1).bitter(0.1).sour(1.0).build()),
    BITTERNESS   (FlavorVector::getBitterness,    tasteProfileWith().body(0.2).bitter(1.0).build()),
    BODY         (FlavorVector::getBody,          tasteProfileWith().sweet(0.2).body(1.0).build()),
    APPLE        (FlavorVector::getApple,         tasteProfileWith().sweet(0.6).body(0.2).sour(0.2).build()),
    BANANA       (FlavorVector::getBanana,        tasteProfileWith().sweet(0.7).body(0.4).sour(0.1).build()),
    CHERRY       (FlavorVector::getCherry,        tasteProfileWith().sweet(0.7).body(0.2).bitter(0.1).sour(0.2).build()),
    CITRUS       (FlavorVector::getCitrus,        tasteProfileWith().sweet(0.3).body(0.1).bitter(0.1).sour(0.7).build()),
    FRUITY       (FlavorVector::getFruity,        tasteProfileWith().sweet(0.6).body(0.2).sour(0.2).build()),
    LEMON        (FlavorVector::getLemon,         tasteProfileWith().sweet(0.2).body(0.1).bitter(0.1).sour(1.0).build()),
    ORANGE       (FlavorVector::getOrange,        tasteProfileWith().sweet(0.5).body(0.2).bitter(0.1).sour(0.5).build()),
    PEAR         (FlavorVector::getPear,          tasteProfileWith().sweet(0.6).body(0.2).sour(0.2).build()),
    RAISINS      (FlavorVector::getRaisins,       tasteProfileWith().sweet(0.7).body(0.4).bitter(0.1).sour(0.1).build()),
    ZEST         (FlavorVector::getZest,          tasteProfileWith().sweet(0.2).body(0.1).bitter(0.3).sour(0.6).build()),
    EARTHY       (FlavorVector::getEarthy,        tasteProfileWith().sweet(0.1).body(0.3).bitter(0.3).smoky(0.2).build()),
    BARLEY       (FlavorVector::getBarley,        tasteProfileWith().sweet(0.2).body(0.6).bitter(0.2).build()),
    BUTTERY      (FlavorVector::getButtery,       tasteProfileWith().sweet(0.5).body(0.8).build()),
    BUTTERSCOTCH (FlavorVector::getButterscotch,  tasteProfileWith().sweet(0.9).body(0.5).build()),
    CANDY        (FlavorVector::getCandy,         tasteProfileWith().sweet(0.9).body(0.2).build()),
    CHOCOLATE    (FlavorVector::getChocolate,     tasteProfileWith().sweet(0.5).body(0.7).bitter(0.3).smoky(0.1).build()),
    CINNAMON     (FlavorVector::getCinnamon,      tasteProfileWith().sweet(0.3).body(0.3).bitter(0.2).abv(0.2).build()),
    COCOA        (FlavorVector::getCocoa,         tasteProfileWith().sweet(0.2).body(0.6).bitter(0.7).smoky(0.1).build()),
    CORN         (FlavorVector::getCorn,          tasteProfileWith().sweet(0.3).body(0.5).bitter(0.1).build()),
    HONEY        (FlavorVector::getHoney,         tasteProfileWith().sweet(0.9).body(0.3).build()),
    TEA          (FlavorVector::getTea,           tasteProfileWith().sweet(0.1).body(0.3).bitter(0.6).build()),
    TOFFEE       (FlavorVector::getToffee,        tasteProfileWith().sweet(0.8).body(0.5).bitter(0.1).build()),
    CLOVE        (FlavorVector::getClove,         tasteProfileWith().sweet(0.2).body(0.3).bitter(0.3).abv(0.3).smoky(0.2).build()),
    COFFEE       (FlavorVector::getCoffee,        tasteProfileWith().sweet(0.1).body(0.6).bitter(0.9).smoky(0.2).build()),
    FLORAL       (FlavorVector::getFloral,        tasteProfileWith().sweet(0.3).body(0.1).build()),
    LICORICE     (FlavorVector::getLicorice,      tasteProfileWith().sweet(0.3).body(0.3).bitter(0.4).smoky(0.1).build()),
    MALTY        (FlavorVector::getMalty,         tasteProfileWith().sweet(0.3).body(0.7).bitter(0.2).build()),
    MINT         (FlavorVector::getMint,          tasteProfileWith().sweet(0.2).body(0.1).bitter(0.2).sour(0.1).build()),
    NUTMEG       (FlavorVector::getNutmeg,        tasteProfileWith().sweet(0.2).body(0.3).bitter(0.3).abv(0.2).smoky(0.1).build()),
    PEATY        (FlavorVector::getPeaty,         tasteProfileWith().body(0.4).bitter(0.2).smoky(1.0).build()),
    PEPPERY      (FlavorVector::getPeppery,       tasteProfileWith().body(0.2).bitter(0.2).abv(0.6).build()),
    ROSES        (FlavorVector::getRoses,         tasteProfileWith().sweet(0.3).body(0.1).build()),
    SPICES       (FlavorVector::getSpices,        tasteProfileWith().sweet(0.2).body(0.3).bitter(0.3).abv(0.3).smoky(0.1).build()),
    SUGAR        (FlavorVector::getSugar,         tasteProfileWith().sweet(1.0).body(0.1).build()),
    TOBACCO      (FlavorVector::getTobacco,       tasteProfileWith().body(0.5).bitter(0.3).smoky(0.8).build()),
    VANILLA      (FlavorVector::getVanilla,       tasteProfileWith().sweet(0.8).body(0.4).build()),
    WOOD         (FlavorVector::getWood,          tasteProfileWith().sweet(0.1).body(0.5).bitter(0.2).smoky(0.6).build()),
    SHERRY       (FlavorVector::getSherry,        tasteProfileWith().sweet(0.4).body(0.5).bitter(0.1).smoky(0.5).sour(0.1).build()),
    BITTER       (FlavorVector::getBitter,        tasteProfileWith().body(0.2).bitter(1.0).build()),
    BRINE        (FlavorVector::getBrine,         tasteProfileWith().body(0.2).bitter(0.2).sour(0.2).build()),
    CREAMY       (FlavorVector::getCreamy,        tasteProfileWith().sweet(0.4).body(0.9).build()),
    GINGER       (FlavorVector::getGinger,        tasteProfileWith().sweet(0.2).body(0.3).bitter(0.2).abv(0.4).sour(0.1).build()),
    HERBAL       (FlavorVector::getHerbal,        tasteProfileWith().sweet(0.1).body(0.2).bitter(0.4).sour(0.1).build()),
    MAPLE        (FlavorVector::getMaple,         tasteProfileWith().sweet(0.9).body(0.4).build()),
    NUTTY        (FlavorVector::getNutty,         tasteProfileWith().sweet(0.3).body(0.7).bitter(0.2).build()),
    OAK          (FlavorVector::getOak,           tasteProfileWith().sweet(0.1).body(0.6).bitter(0.3).smoky(0.6).build()),
    SALTY        (FlavorVector::getSalty,         tasteProfileWith().body(0.1).bitter(0.1).build()),
    SMOKEY       (FlavorVector::getSmokey,        tasteProfileWith().body(0.3).bitter(0.2).smoky(1.0).build()),
    SOUR         (FlavorVector::getSour,          tasteProfileWith().body(0.1).bitter(0.1).sour(1.0).build()),
    SPICY        (FlavorVector::getSpicy,         tasteProfileWith().body(0.2).bitter(0.2).abv(0.5).build()),
    SWEET        (FlavorVector::getSweet,         tasteProfileWith().sweet(0.9).body(0.3).build()),
    CARAMEL      (FlavorVector::getCaramel,       tasteProfileWith().sweet(0.8).body(0.4).bitter(0.1).build());

    private final Function<FlavorVector, Double> extractor;
    private final TasteProfile weight;

    FlavorDescriptor(Function<FlavorVector, Double> extractor, TasteProfile weight) {
        this.extractor = extractor;
        this.weight = weight;
    }

    double valueFrom(FlavorVector vector) {
        return extractor.apply(vector);
    }

    TasteProfile tasteProfile() {
        return weight;
    }

    private static TasteProfile.TasteProfileBuilder tasteProfileWith() {
        return TasteProfile.builder();
    }
}
