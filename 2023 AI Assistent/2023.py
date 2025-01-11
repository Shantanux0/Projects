import pyttsx3  # Module for text-to-speech conversion
import speech_recognition as sr  # Module for speech recognition
import datetime
import wikipedia  # Module for fetching information from Wikipedia
import webbrowser
import os
import sqlite3

# Initialize the text-to-speech engine
engine = pyttsx3.init('sapi5')
voices = engine.getProperty('voices')
engine.setProperty('voice', voices[1].id)
engine.setProperty('rate', 150)  # Set the speaking rate to medium (default is 200)

def speak(audio):
    """Function to speak out the audio"""
    engine.say(audio)
    engine.runAndWait()

def takeCommand():
    """Function to recognize speech"""
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print("Listening...")
        r.pause_threshold = 1
        audio = r.listen(source)

    try:
        print("Recognizing...")
        query = r.recognize_google(audio, language='en-in')
        print(f"You said: {query}\n")
    except Exception as e:
        print("Sorry, I couldn't understand. Please say that again...")
        return "None"
    return query

def get_user_name():
    """Function to get the user's name"""
    speak("Hello! What's your name?")
    user_name = takeCommand()
    speak(f"Nice to meet you, {user_name}!")
    return user_name

def login_choice():
    """Function to ask the user whether they want to use basic features or login to unlock more features"""
    speak("Would you like to use basic features or log in to unlock perimum features?")
    speak("Please say 'basic' for basic features or 'login' to unlock more features.")
    choice = takeCommand().lower()
    while choice not in ['basic', 'login']:
        speak("Sorry, I didn't understand. Please say 'basic' or 'login'.")
        choice = takeCommand().lower()
    return choice

def wishMe(user_name):
    """Function to greet the user"""
    hour = datetime.datetime.now().hour
    if 0 <= hour < 12:
        speak(f"Good Morning, {user_name}!")  
    elif 12 <= hour < 18:
        speak(f"Good Afternoon, {user_name}!")
    else:
        speak(f"Good Evening, {user_name}!")
    speak("I am ell. Please tell me how may I help you")

def get_login():
    """Function to ask the user if they want to log in to access more features"""
    speak("Would you like to log in to access more features?")
    speak("Please say 'login' to log in or 'skip' to continue with basic features.")
    choice = takeCommand().lower()
    while choice not in ['login', 'skip']:
        speak("Sorry, I didn't understand. Please say 'login' or 'skip'.")
        choice = takeCommand().lower()
    return choice
def create_table():
    """Function to create a table in the database"""
    try:
        # Connect to the SQLite database
        conn = sqlite3.connect('my_database.db')
        c = conn.cursor()
        
        # Get user input for table data
        speak("Please enter the following details to create a new record:")
        name = input("Enter name: ")
        age = int(input("Enter age: "))
        address = input("Enter address: ")
        marks = int(input("Enter marks: "))
        
        # Execute SQL query to create table and insert data
        c.execute('''CREATE TABLE IF NOT EXISTS my_table (
                        id INTEGER PRIMARY KEY,
                        name TEXT,
                        age INTEGER,
                        address TEXT,
                        marks INTEGER
                    )''')
        c.execute("INSERT INTO my_table (name, age, address, marks) VALUES (?, ?, ?, ?)", (name, age, address, marks))
        conn.commit()
        
        # Fetch and display data from the table
        c.execute("SELECT * FROM my_table")
        rows = c.fetchall()
        if rows:
            speak("Here is the data in the table:")
            for row in rows:
                speak(f"ID: {row[0]}, Name: {row[1]}, Age: {row[2]}, Address: {row[3]}, Marks: {row[4]}")
        else:
            speak("The table is empty.")
        
        # Close the database connection
        conn.close()
        speak("The table has been created and data has been inserted successfully!")
    except Exception as e:
        speak("An error occurred while creating the table.")
        print(e)

  

def personalized_account():
    """Function to create and manage personalized account"""
    speak("Let's log in to access more features.")
    
    while True:
        choice = get_login()
        if choice == 'login':
            speak("Please enter your username:")
            username = input()  # Modified to get user input directly
            
            speak("Please enter your password:")
            password = input()  # Modified to get user input directly

            # Implement login functionality here
            # Check if username and password are valid
            if username == 'shantanu' and password == '100':
                speak("Voilà! Your personalized account is now ready to rock and roll!")
                
                speak("Congratulations! You have unlocked the following features:")
                speak("- Email sending")
                speak("- Creating a database and table")
                speak("- and all basic features")
                
                action = choose_action()  # Assuming choose_action() function exists
                if action == 'email':
                    # Code to handle email sending feature
                    pass
                elif action == 'database':
                    create_table()  # Call the function to create a table
                elif action == 'basic':
                    basic_features()
                    # Code to handle basic features
                    pass
                
                break  # Break out of the while loop if login successful
            else:
                speak("Invalid username or password. Please try again.")
        else:
            speak("Skipping login. Continuing with basic features.")
            break  # Break out of the while loop if user chooses to skip login

def basic_features():
    """Function to provide basic features"""
    while True:
        query = takeCommand().lower()

        if 'wikipedia' in query:
            speak('Searching Wikipedia...')
            query = query.replace("wikipedia", "")
            results = wikipedia.summary(query, sentences=2)
            speak("According to Wikipedia")
            speak(results)
        elif 'open youtube' in query:
            webbrowser.open("https://www.youtube.com/")
        elif 'open google' in query:
            webbrowser.open("https://www.google.com/")
        elif 'open stack' in query:
            webbrowser.open("https://stackoverflow.com/")
        elif 'play music' in query:
            music_dir = r'C:\Users\91902\Music\nucleya'  
            songs = os.listdir(music_dir)
            if songs:  
                os.startfile(os.path.join(music_dir, songs[0]))  # Play the first song in the directory
            else:
                speak("No music files found in the directory.")
        elif 'the time' in query:
            strTime = datetime.datetime.now().strftime("%H:%M:%S")
            speak(f"Sir, the time is {strTime}")
        elif 'stop' in query:
            speak("Stopping the program.")
            break  # Break out of the while loop and stop the program
def choose_action():
    """Function to choose an action after successful login"""
    speak("What would you like to do?")
    speak("Please say 'email' for email sending, 'database' for creating a database and table, or 'basic' for basic features.")
    choice = takeCommand().lower()
    while choice not in ['email', 'database', 'basic']:
        speak("Sorry, I didn't understand. Please say 'email', 'database', or 'basic'.")
        choice = takeCommand().lower()
    return choice

if __name__ == "__main__":
    user_name = get_user_name()
    choice = login_choice()
    if choice == 'basic':
        wishMe(user_name)
        basic_features()
    elif choice == 'login':
        personalized_account()
