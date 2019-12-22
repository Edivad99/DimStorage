local component = require("component")
local chest = component.dimchest

print("DimStorge API")
print("Owner: " .. chest.getOwner())
print("Frequency: " .. chest.getFrequency())
print("Is locked: " .. (chest.isLocked() and 'true' or 'false'))
print("Is public: " .. (chest.isPublic() and 'true' or 'false'))
print("Size inventory: " .. chest.getSizeInventory())
print("Can access: " .. (chest.canAccess() and 'true' or 'false'))

print("-------------------------------")

print("Print all item inside DimChest")
for i=0, chest.getSizeInventory()-1, 1
do 
  local item = chest.getStackInSlot(i)
  if item ~= nil then
    print(item.name .. " - " .. item.label .. " (" .. item.size .. ")")
  end
end

print("-------------------------------")

print("Owner: " .. chest.getOwner())
local newOwner = chest.toggleOwner()
if newOwner == true then
  print("Successful modification")
  print("New owner: " .. chest.getOwner())
else
  print("Change not successful")
end

print("-------------------------------")

print("Frequency: " .. chest.getFrequency())
chest.changeFrequency(chest.getFrequency() + 1)
print("New frequency: " .. chest.getFrequency())

print("-------------------------------")

print("Is locked: " .. (chest.isLocked() and 'true' or 'false'))
local newLock = chest.toggleLock()
if newLock == true then
  print("Successful modification")
  print("Is locked: " .. (chest.isLocked() and 'true' or 'false'))
else
  print("Change not successful")
end

print("-------------------------------")
