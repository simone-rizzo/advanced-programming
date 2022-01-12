-- Exporting Dictionary module with all the functions that we want.
module Dictionary (
    Dictionary,     
    Dictionary.empty,
    Dictionary.insert,
    Dictionary.lookup,
    Dictionary.keys,
    Dictionary.values,
    Dictionary.merge,
    Dictionary.equalKeys
) where

-- Declaration of the type Dictionary
data Dictionary a b = Dict [(a, [b])] deriving (Show)

-- Return an empty Dictionary
empty :: () -> Dictionary a b
empty () = Dict []

-- Insert in the dictionary the element v with key k by preserving the
-- well-formed dictionary property 
insert :: Eq a => Dictionary a b -> a -> b -> Dictionary a b
insert (Dict values) k v = Dict (insert' values)
    where
        insert' [] = [(k, [v])]
        insert' ((a, b):l)
         | k == a = (a, v:b):l
         |otherwise = (a, b) : insert' l

-- return a value Maybe[b] which is the list of elements
-- with key k, if such list exists in dict, and Nothing otherwise
lookup :: Eq a => Dictionary a b -> a -> Maybe [b]
lookup (Dict values) k = myfun values
    where
        myfun [] = Nothing
        myfun ((a, b):l)
         | k == a = Just b
         | otherwise = myfun l

-- returning a list containing the keys of dict.
keys :: Dictionary a b -> [a]
keys (Dict values) = getKeys values []
    where
      getKeys [] res = res
      getKeys ((a,b):l) res = getKeys l (a : res)

-- returning a single list containing all the values present in dict.
values :: Dictionary a b -> [b]
values (Dict val) = getvalues val []
    where
      getvalues [] res = res
      getvalues ((a,b):l) res = getvalues l (b ++ res) 


merge :: Eq a => Dictionary a b -> Dictionary a b -> Dictionary a b
merge (Dict dict1) (Dict dict2) = foldl (\acc x -> insertl acc x) (empty ()) (dict1 ++ dict2)


-- Insert in the dictionary a list with key k by preserving the
-- well-formed dictionary property 
insertl :: Eq a => Dictionary a b -> (a,[b]) -> Dictionary a b
insertl (Dict values) (k,v) = Dict (insertl' values)
    where
        insertl' [] = [(k, v)]
        insertl' ((a, b):l)
         | k == a = (a, (v ++ b)):l
         |otherwise = (a, b) : insertl' l

-- Check if an element is inside a list
exist :: Eq a => a -> [a] -> Bool
exist val [] = False
exist val (x:xs)
  | val == x = True
  | otherwise = exist val xs

-- Get the lenght of a list
lenght:: [a] -> Integer
lenght l = lenght' 0 l
    where
      lenght' acc [] = acc
      lenght' acc (x:xs) = lenght' (acc+1) xs

-- The lenght of the keys of a given dictionary
keyslen :: Dictionary a b -> Integer
keyslen (Dict values) = lenght (keys (Dict values))

-- This method check if the two lists are equal.
-- The list are equal if they have the same lenght and then
-- if all the elements of one in inside the other.
equals_list :: Eq a => [a] -> [a] -> Bool
equals_list [] [] = True
equals_list l1 l2
 | lenght l1 == lenght l2 && equal_elements l1 l2 = True
 | otherwise = False
 where
    equal_elements [] [] = True
    equal_elements [] l2 = True
    equal_elements (x:xs) l2
      | (exist x l2) = equal_elements xs l2
      | otherwise = False

-- Check if the two Maybe list are equal.
equal_maybe_lst :: Eq a =>  Maybe [a] -> Maybe [a] -> Bool
equal_maybe_lst Nothing Nothing = True
equal_maybe_lst Nothing (Just x) = False
equal_maybe_lst (Just x) Nothing = False
equal_maybe_lst (Just l1) (Just l2) = equals_list l1 l2

-- Check if the two dictionary are equal:
-- Firs we check if they contains the same keys, if that is true we proceed by
-- control if also the values are the same. otherwise we return false.
equal_dict :: (Eq a, Eq b) => Dictionary a b -> Dictionary a b -> Bool
equal_dict (Dict dct1) (Dict dct2)
 | equals_list (keys (Dict dct1)) (keys (Dict dct2)) = keylistcheck (keys (Dict dct1)) (Dict dct1) (Dict dct2)
 | otherwise = False

-- Check if for each key the two dictionary have the same set.
keylistcheck :: (Eq a, Eq b) => [a] -> Dictionary a b -> Dictionary a b -> Bool
keylistcheck [] (Dict dict1) (Dict dict2) = True
keylistcheck (x:xs) (Dict dict1) (Dict dict2)
 -- if the two sets for a given k are equal thats good and we continue the recursion.
 | equal_maybe_lst (Dictionary.lookup (Dict dict1) x) (Dictionary.lookup (Dict dict2) x) = keylistcheck xs (Dict dict1) (Dict dict2)
 -- otherwise its false
 | otherwise = False

instance (Eq a, Eq b) => Eq (Dictionary a b) where
  (==) (Dict b) (Dict b') = equal_dict (Dict b) (Dict b')

-- Check if the Dictionaries contain the same keys.
equalKeys :: (Eq a, Eq b) => Dictionary a b -> Dictionary a b -> Bool
equalKeys (Dict d1) (Dict d2) = equals_list (keys (Dict d1)) (keys (Dict d2))

