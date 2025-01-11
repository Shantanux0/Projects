User
import pyttsx3  # Module for text-to-speech conversion
import speech_recognition as sr  # Module for speech recognition
import datetime
import wikipedia  # Module for fetching information from Wikipedia
import webbrowser
import os
import googletrans
from googletrans import Translator

# Initialize the text-to-speech engine
engine = pyttsx3.init('sapi5')
voices = engine.getProperty('voices')
engine.setProperty('voice', voices[1].id)
engine.setProperty('rate', 150)  # Set the speaking rate to medium (default is 200)

translator = Translator()

def speak(audio):
    """Function to speak out the audio"""
    engine.say(audio)
    engine.runAndWait()

def get_user_name():
    """Function to get the user's name"""
    speak("Hello! What's your name?")
    user_name = takeCommand()
    speak(f"Nice to meet you, {user_name}!")
    return user_name

def login_choice():
    """Function to ask the user whether they want to log in with a general or personalized account"""
    speak("Would you like to log in with a general account or a personalized account?")
    speak("Please say 'general' for a general account or 'personalized' for a personalized account.")
    choice = takeCommand().lower()
    while choice not in ['general', 'personalized']:
        speak("Sorry, I didn't understand. Please say 'general' or 'personalized'.")
        choice = takeCommand().lower()
    return choice

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

if __name__ == "__main__":
    user_name = get_user_name()
    choice = login_choice()
    if choice == 'general':
        wishMe(user_name)
        while True:
            query = takeCommand().lower()

            if 'wikipedia' in query:
                speak('Searching Wikipedia...')
                query = query.replace("wikipedia", "")
                results = wikipedia.summary(query, sentences=2)
                speak("According to Wikipedia")
                print(results)
                speak(results)
            elif 'open youtube' in query:
                webbrowser.open("youtube.com")
            elif 'open google' in query:
                webbrowser.open("google.com")
            elif 'open stack' in query:
                webbrowser.open("stackoverflow.com")
            elif 'play music' in query:
                music_dir = r'C:\Users\91902\Music\nucleya'  
                songs = os.listdir(music_dir)
                print(songs)
                if songs:  
                    os.startfile(os.path.join(music_dir, songs[5]))
                else:
                    speak("No music files found in the directory.")
            elif 'the time' in query:
                strTime = datetime.datetime.now().strftime("%H:%M:%S")
                speak(f"Sir, the time is {strTime}")
            elif 'open code' in query:
                #codepath = 
                os.startfile(codePath)
            elif 'translate' in query:
                speak("What would you like to translate?")
                text_to_translate = takeCommand()
                speak("Which language would you like to translate to?")
                target_language = takeCommand().lower()  # You can specify the target language here
                translated_text = translator.translate(text_to_translate, dest=target_language)
                speak(f"The translation is: {translated_text.text}")
            elif 'stop' in query:
                speak("Stopping the program.")
                break  # Break out of the while loop and stop the program

            def personalized_account():
                  """Function to create and manage personalized account"""
    speak("Let's create your personalized account. Please provide the following details.")
    
    user_data = {}  # Dictionary to store user data
    
    speak("What is your name?")
    name = takeCommand()
    user_data['Name'] = name
    
    speak("How old are you?")
    age = takeCommand()
    user_data['Age'] = age
    
    speak("What is your address?")
    address = takeCommand()
    user_data['Address'] = address
    
    speak("What marks have you scored?")
    marks = takeCommand()
    user_data['Marks'] = marks
    
    speak("Your personalized account has been created successfully!")
    
    # Display the user data
    speak("Here are your account details:")
    for key, value in user_data.items():
        speak(f"{key}: {value}")
    
    # Further actions for personalized account can be added here
elif choice == 'personalized':
        # Add personalized account functionality here
    pass  # Placeholder for personalized account functionality

