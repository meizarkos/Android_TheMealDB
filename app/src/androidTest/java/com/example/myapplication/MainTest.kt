package com.example.myapplication

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    IngredientListTest::class,
    AllRecipesTest::class,
    DetailRecipeTest::class
)
class AllTestsSuite { // This class remains empty, it is used only as a holder for the above annotations
}