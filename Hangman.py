import random

hangman_art = {0: ("   ",
                                   "   ",
                                   "   "),
                             1: (" o ",
                                   "   ",
                                   "   "),
                             2: (" o ",
                                   " | ",
                                   "   "),
                             3: (" o ",
                                   "/| ",
                                   "   "),
                             4: (" o ",
                                  "/|\\",
                                   "   "),
                              5: (" o ",
                                   "/|\\",
                                   "/  "),
                              6: (" o ",
                                   "/|\\",
                                   "/ \\")}

words = ("alligator", "ant", "armadillo", "baboon", "bat", "bear", "beaver", "bee", "boar", "buffalo", "butterfly", "camel", "cat", "caterpillar", "cheetah", "chicken", "cobra", "cockroach", "crab", "crocodile", "crow", "deer", "dinosaur", "dog", "dolphin", "donkey", "dragonfly", "duck", "eagle", "elephant", "fish", "flamingo", "fly", "fox", "frog", "giraffe", "goat", "goldfish", "goose", "gorilla", "grasshopper", "hamster", "hedgehog", "hippopotamus", "hornet", "horse", "human", "jaguar", "jellyfish", "kangaroo", "koala", "lemur", "leopard", "lion", "llama", "lobster", "mantis", "monkey", "moose", "mosquito", "mouse", "mule", "octopus", "ostrich", "owl", "panda", "panther", "parrot", "penguin", "pig", "pigeon", "pony", "rabbit", "raccoon", "rat", "raven", "reindeer", "rhinoceros", "rook", "salmon", "sardine", "scorpion", "seahorse", "seal", "shark", "sheep", "skunk", "snail", "snake", "spider", "squid", "squirrel", "tiger", "turkey", "turtle", "viper", "wasp", "weasel", "whale", "wildcat", "wolf", "woodpecker", "worm", "zebra")

def display_man(wrong_guesses):
    print("**********")
    for line in hangman_art[wrong_guesses]:
        print(line)
    print("**********")

def display_hint(hint):
    print(" ".join(hint))

def display_answer(answer):
    print(" ".join(answer))

def main():
    answer = random.choice(words)
    hint = ["_"] * len(answer)
    wrong_guesses = 0
    guessed_letters = set()
    is_running = True

    while is_running:
        display_man(wrong_guesses)
        display_hint(hint)
        guess = input("Enter a letter: ").lower()

        if len(guess) != 1 or not guess.isalpha():
            print("Invalid input")
            continue

        if guess in guessed_letters:
            print(f"{guess} is already guessed")
            continue

        guessed_letters.add(guess)

        if guess in answer:
            for i in range(len(answer)):
                if answer[i] == guess:
                    hint[i] = guess
        else:
            wrong_guesses += 1

        if "_" not in hint:
            display_man(wrong_guesses)
            display_answer(answer)
            print("YOU WIN!")
            is_running = False
        elif wrong_guesses >= len(hangman_art) - 1:
            display_man(wrong_guesses)
            display_answer(answer)
            print("YOU LOSE!")
            is_running = False

if __name__ == "__main__":
    main()