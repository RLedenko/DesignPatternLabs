
def mymax(iterable, key = lambda x : x):
    max_x = max_key = None

    for x in iterable:
        x_key = key(x)
        if(max_x == None or x_key > max_key):
            max_x = x
            max_key = x_key
            
    return max_x

# Najduži string u listi
strlen = lambda x : len(x)
longest_string = mymax(
    ["Gle", "malu", "vocku", "poslije", "kise", "Puna", "je", "kapi", "pa", "ih", "njise"], 
    strlen
)
print("Najduži string uporabom lambde za duljinu:", longest_string)

# Poziv sa defaultnim ključem
maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
maxchar = mymax("Suncana strana ulice")
maxstring = mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"
])
print("Maxint:", maxint, "\nMaxchar:", maxchar, "\nMaxstring:", maxstring)

# Najskuplji proizvod u pekari
D = {
    "burek"  : 8,
    "buhtla" : 5
}
maxpekara = mymax(D, D.get)
print("Najskuplji proizvod u pekari:", maxpekara)

# Osobe
people = [
    ("Luigi", "Antić"),
    ("Roman", "Vulin"),
    ("Šime", "Roca"),
    ("Roko", "Marin"),
    ("Grgo", "Friganović"),
    ("Ante", "Cukrov")
]
last_lex_person = mymax(people)
print("Leksikografski zadnja osoba je:", last_lex_person)