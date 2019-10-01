# CS3130-2019-A2-Hangman
2019 Assignment 2 Hangman Game

If you are unfamiliar with the Game Hangman familiarize yourself with the game at the following webpage: 
http://hangman-api.herokuapp.com

In the game you guess letters trying to uncover a mystery word. If you get all of the letters in the word you win. If you guess a letter that is not in the work you get a strike (in the form of a body part revealed from the hangman character), make 7 incorrect guesses before uncovering all the letters in the game and you lose.

![Image of Hangman](hangman_media/stage7.png)

For your implementation of the game you will use the API found at: 
http://hangman-api.herokuapp.com/api

Notice that the API contains all the functionality required to play the game. Thus your app will only have to pass information to the API and display the results to the user. 

Note that the urls provided by the API all reference from the base url: http://hangman-api.herokuapp.com

Thus to start a game you should create a Http POST connection to the url: http://hangman-api.herokuapp.com/hangman and then you will receive back in a JSON string { "hangman":"____","token":"xx"}. Where the length of the string associated with the hangman tag is the length of the word to be guessed in the game. The token is required for future calls into the API so that you can refer to the same game that you just started.

See the rest of the API for how you can guess a letter and / or as for a hint. 

You should only ask for the Solution after a game has been completed (either all letters guessed, or 7 incorrect guesses made). 

For debugging purposes you can complete an API call through the terminal command 'curl' (or similar command) by running: curl -X POST http://hangman-api.herokuapp.com/hangman which starts a game. To get your API calls working correctly and your JSON parsing, the use of Log.d to show the raw url or raw JSON text easily and the use of an application such as curl or HTTPie to execute API calls is often helpful. 

Follow the notes from class on how to parse JSON strings and make http connections.

For this assignment to keep things as general as possible you should use the AsyncTask class to handle your API calls. There are some specialized libraries, however the AsyncTask can be used widely and so it is appropriate for this task and others you may encounter in your assignments.

Find in the hangman_media directory images associated with the game, which you may display as the user makes incorrect guesses as they play the game. 

A single activity is appropriate for this game, although you may wish to add other classes as you see fit.

Submission Instructions:

Create your solution inside its own directory inside this repository. Thus your repository will look something like this, where DIR is the main directory of this repository and Your_Solution is your Android Studio project main directory:

DIR/
  .gitignore
  README.md
  hangman_media/
  Your_Solution/

Submit your assignment using git before the deadline. 

Grading: 

10 PTS Total

2 PTS Code Readability - proper variable names, your repository matches the desired format, proper access modifiers, no string literals

4 PTS Design - Proper use of classes and interfaces, no overily complex classes or methods, appropriate information flow

4 PTS Functionality - You program allows a game of hangman to be played. The screen rotates without issues. Starting new games is 
appropriate, etc. 






