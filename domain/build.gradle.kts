// Copyright 2023, Google LLC, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0


plugins {
    id("app.tivi.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)

                api(projects.data.models)

                implementation(projects.data.db) // remove this eventually
                implementation(projects.data.legacy) // remove this eventually
                implementation(projects.data.episodes)
                implementation(projects.data.followedshows)
                implementation(projects.data.popularshows)
                implementation(projects.data.recommendedshows)
                implementation(projects.data.relatedshows)
                implementation(projects.data.search)
                implementation(projects.data.showimages)
                implementation(projects.data.shows)
                implementation(projects.data.traktauth)
                implementation(projects.data.traktusers)
                implementation(projects.data.trendingshows)
                implementation(projects.data.watchedshows)

                implementation(projects.api.tmdb)

                api(libs.paging.common)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
