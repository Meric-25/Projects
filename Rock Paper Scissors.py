import random

my_choice = input("Please choose from the list (Rock, Paper, Scissors): ").capitalize()

choices = ["Rock", "Paper", "Scissors"]
pc_choice = random.choice(choices)

print("Computer chose:", pc_choice)

if my_choice == pc_choice:
    print("It's a tie. You both selected", my_choice)

elif my_choice == "Rock":
    if pc_choice == "Scissors":
        print("You win!")
    else:
        print("You lose!")

elif my_choice == "Paper":
    if pc_choice == "Rock":
        print("You win!")
    else:
        print("You lose!")

elif my_choice == "Scissors":
    if pc_choice == "Paper":
        print("You win!")
    else:
        print("You lose!")

else:
    print("Invalid choice. Please choose Rock, Paper, or Scissors.")
