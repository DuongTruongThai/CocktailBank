package com.example.cocktailbank.util

class Constants {

    companion object {
        const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

        //Bundle key
        const val BUNDLE_KEY_1 = "drinkBundle1"
        const val BUNDLE_KEY_2 = "drinkBundle2"

        //API Query Keys
        const val QUERY_ID = "i"
        const val QUERY_INGREDIENT = "i"
        const val QUERY_ALCOHOLIC = "a"
        const val QUERY_CATEGORY = "c"
        const val QUERY_GLASS = "g"
        const val QUERY_SEARCH_BY_NAME = "s"

        //ROOM Database
        const val DATABASE_NAME = "cocktails_database"
        const val COCKTAILS_TABLE = "cocktails_table"
        const val FAVORITE_COCKTAILS_TABLE = "favorite_cocktails_table"

        //Bottom Sheet and Preferences
        const val CATEGORY_FILTER = "c"
        const val GLASS_FILTER = "g"
        const val INGREDIENTS_FILTER = "i"
        const val ALCOHOLIC_TYPE_FILTER = "a"

        const val DEFAULT_FILTER_TYPE = "a"
        const val DEFAULT_FILTER_NAME = "Alcoholic"

        const val PREFERENCES_NAME = "cocktails preferences"
        const val PREFERENCES_FILTER_TYPE = "filterType"
        const val PREFERENCES_FILTER_NAME = "filterName"
        const val PREFERENCES_FILTER_ID = "filterId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"
    }

}