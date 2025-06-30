import random

pc_choice = random.randint(1,3)
is_running = True
number = 0

print("Welcome to the 21 game")

while is_running:
    input_number = float(input("Enter a number between 1 and 3: "))
    if input_number <=3 and input_number > 0:
        number += input_number
        print(number)
        if number < 21:
            pass
        elif number > 21:
            print("You Lose!")
            is_running = False
            break
        elif number == 21:
            print("You Won!")
            is_running = False
            break
        number += pc_choice
        print(f"Pc choose {pc_choice}")
        print(number)
        if number < 21:
            pass
        elif number > 21:
            print("You won!")
            is_running = False
        elif number == 21:
            print("You Lose!")
            is_running = False

        
    else:
        print("Invalid number")
        continue


