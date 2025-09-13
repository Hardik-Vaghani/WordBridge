package com.devtools.wordbridge.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppControllerRootClass() : Application() { }


/**
INSERT INTO words (primaryWord, wordMeaning, secondaryWord, secondaryWordPronunciation)
VALUES
('Book', 'A set of pages with written or printed content', 'Kitab', 'Ki-taab'),
('Apple', 'A type of fruit', 'Seb', 'Sayb'),
('Computer', 'An electronic device for processing data', 'Sanganak', 'Sang-a-nak');
* */