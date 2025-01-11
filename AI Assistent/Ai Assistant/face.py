import cv2
import dlib
import numpy as np

# Load pre-trained dlib face detector and facial landmark predictor
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')
face_rec_model = dlib.face_recognition_model_v1('dlib_face_recognition_resnet_model_v1.dat')

# Initialize webcam
cap = cv2.VideoCapture(0)

# Load known faces and their encodings
known_faces = []
known_names = []

# Example of loading known faces (you can replace these with your own images and names)
known_face_image = cv2.imread('path_to_known_face_image.jpg')
known_face_image = cv2.cvtColor(known_face_image, cv2.COLOR_BGR2RGB)
faces = detector(known_face_image)
if faces:
    shape = predictor(known_face_image, faces[0])
    face_encoding = np.array(face_rec_model.compute_face_descriptor(known_face_image, shape))
    known_faces.append(face_encoding)
    known_names.append('Known Person Name')

while True:
    ret, frame = cap.read()
    if not ret:
        break
    
    # Convert frame to RGB
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    
    # Detect faces
    faces = detector(rgb_frame)
    
    for face in faces:
        # Get the landmarks/parts for the face
        shape = predictor(rgb_frame, face)
        
        # Compute the face encoding
        face_encoding = np.array(face_rec_model.compute_face_descriptor(rgb_frame, shape))
        
        # Compare the face encoding to known faces
        matches = []
        for known_face in known_faces:
            match = np.linalg.norm(known_face - face_encoding) < 0.6
            matches.append(match)
        
        name = "Unknown"
        if True in matches:
            first_match_index = matches.index(True)
            name = known_names[first_match_index]
        
        # Draw a rectangle around the face
        x, y, w, h = (face.left(), face.top(), face.width(), face.height())
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
        
        # Draw the name of the person
        cv2.putText(frame, name, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
    
    # Display the frame
    cv2.imshow('Face Recognition', frame)
    
    # Break the loop if 'q' is pressed
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the webcam and close windows
cap.release()
cv2.destroyAllWindows()
