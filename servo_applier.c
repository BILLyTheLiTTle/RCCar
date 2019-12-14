/* This tiny program is important because I had a lot of problems communicating between Springboot and servo motor.
The solution is to write to a file from the Springboot framework and this tiny program reads this file and applies the
value to the Servo Blaster.
BUT, there is a trick. Sometimes the servod gets killed and I have to start it again!

The process is:
- CChaneg to root user
- Build (gcc -o xsa the_current_name_of_this_file.c) this executable if it is not already built.
- Run the program (./xsa).
- Start Springboot (./gradlew bootrun).
- Connect the controller.
- If the servo motor doesn't react to the controller's commands kill xsa and run it again.
*/
#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>

#define LENGTH 3

bool check_equality(char*, char*);
char old_file_text [LENGTH], new_file_text[LENGTH];

int main(int argc, char const *argv[]) {
  FILE *fileStream;
  char *command[50];

  system("sudo killall servod");
  sleep(4);
  system("sudo servod");
  sleep(2);

  while (true) {
    fileStream = fopen ("servo.txt", "r");
    fgets (new_file_text, 4, fileStream);
    fclose(fileStream);


    if(!check_equality(new_file_text, old_file_text)) {
      if(new_file_text[2]=='\n') {
        printf("%c%c\n", new_file_text[0], new_file_text[1]);
        snprintf(command, sizeof(command), "echo %d=%c%c > /dev/servoblaster", 5, new_file_text[0], new_file_text[1]);
      }
      else {
        printf("%s\n", new_file_text);
        snprintf(command, sizeof(command), "echo %d=%s > /dev/servoblaster", 5, new_file_text);
      }
      system(command);
      memcpy( old_file_text, new_file_text, sizeof(old_file_text) );
    }
  }

  return 0;
}

bool check_equality(char *arr1, char *arr2) {
  for (int i = 0; i < LENGTH; i++)
          if (arr1[i] != arr2[i])
              return false;
  return true;
}
