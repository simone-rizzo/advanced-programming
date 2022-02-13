import Dictionary
import Data.Char
import Data.List
import Text.Printf (printf)

-- Apply the ciao hashing: at first it lowercase the string and then sort alphabetically.
ciaoword :: String -> String
ciaoword s = sort (lowerString s)

-- Convert the string to lowercase one
lowerString :: [Char] -> String
lowerString = map toLower

-- This function create a dictionary with the couples (ciao(word), word) from a text file.
readDict :: FilePath -> IO (Dictionary String String)
readDict filename = do
    -- here we read the file
    file_content <- readFile filename
    -- With foldl we add the couples (ciao(word), word) inside an empty dictionary.
    return (foldl (\acc w -> Dictionary.insert acc (ciaoword w) w) (empty ()) (words file_content)) -- with words we obtain the list of words so [String]

-- Write all the keys with the lenght of the list associated inside a file.
writeDict :: Dictionary String String -> FilePath -> IO ()
writeDict dct filename = do
    -- We compute for each key the lenght of the list and then we concatenate all the results. 
    let rows = concatMap (\k -> case Dictionary.lookup dct k of
          Nothing -> ""
          Just values -> k ++ "," ++ show (length values) ++ "\n") (keys dct)
    -- here we write all inside a file.
    writeFile filename rows


main :: IO ()
main = do
    d1 <- readDict "./aux_files/anagram.txt"
    d2 <- readDict "./aux_files/anagram-s1.txt"
    d3 <- readDict "./aux_files/anagram-s2.txt"
    d4 <- readDict "./aux_files/margana2.txt"
    printf "Is d1 different from d4:\n%s\nDo they have the same keys:\n%s\n" (show (d1 /= d4)) (show (Dictionary.equalKeys d1 d4))
    -- Print: Dictionary d1 is equal to the merge of dictionaries d2 and d3;
    printf "Is d1 equal to (d2 merged with d3):\n%s\n" (show (d1 == merge d2 d3))
    writeDict d1 "./anag-out.txt"
    writeDict d4 "./gana-out.txt"

