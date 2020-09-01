import json
import csv
import pandas as pd
import pickle


#jaccard

users_friends={}
users=[]
user_matrix={}
user_neighbours={}
sum_similarities={}
f="C:\Users\lenovo\Desktop\App_Rec\src\sample\datasets\movielens100k_data.txt"



dst_file="ml100kfriend.dat"
users_friends = pickle.load(open(dst_file, "rb") , encoding="bytes")



df = pd.read_csv(f,sep='\t', header=None)

for i, j in df.iterrows():

  user=j[0]

  users.append(user)

users = list(dict.fromkeys(users))
print(len(users))


for user in users:
  user_neighbours[user]=[]


#construire la matrice user_user

i=0
cpt=0
b=0
uslist=users
while users != [] :
  b +=1
  utilisateur=users[i]

  friends1=users_friends[utilisateur]
  f=len(friends1)
  friendset1=set(friends1)
  for us in range(1,len(users)):
    inter=0
    friends2=users_friends[users[us]]
    friendset2=set(friends2)
    
    inter=len(friendset1 & friendset2)

    if inter != 0:
    # calculer pour chaque 2 user la distance 
      union=f+len(friends2)-inter

      #maxi=max(len(friends1), len(friends2))
    #enregistrer la distance dans une matrice 

      dist=1- (inter/union)
      #dist=1- (inter/maxi)
      user_matrix[ (utilisateur, users[us]) ]=dist

      user_neighbours[utilisateur].append(users[us])


      user_neighbours[users[us]].append(utilisateur)




      tab=sum_similarities.keys()
      if utilisateur in tab:
        sum_similarities[utilisateur]=sum_similarities[utilisateur]+(1-dist)
      else:
        sum_similarities[utilisateur]=1- dist
      if users[us] in tab:
        sum_similarities[users[us]]=sum_similarities[users[us]]+(1- dist)
      else:
        sum_similarities[users[us]]=1- dist
    cpt=cpt+1
    if cpt % 1000000 == 0:
      print(cpt)
      print(len(users))
  users.remove(utilisateur)
print(b)





f="C:\Users\lenovo\Desktop\App_Rec\src\sample\datasets\movielens100k_data.txt"

user_item={}
user_avg={}
users=[]







df = pd.read_csv(f,sep='\t', header=None)
print(df.head())

d=df.groupby(0).mean()
print(d.head())

for i, j in d.iterrows():

  user=i
  stars=j[2]
  user_item[user]={}
  users.append(user)
  user_avg[user]=stars

print(len(user_avg.keys()))

del d

for i, j in df.iterrows():

  user=j[0]
  business=j[1]
  stars=j[2]
  user_item[user][business]=stars

prediction=[]
reel=[]

key=user_matrix.keys()

filelist=[]

cpt=0
#pour chaque utilisateur on fait les predictions pour les items qu'il a evalué 
for user in users:
  moyenne_principale=user_avg[user]
  items=user_item[user]

  if user in sum_similarities.keys():

    
    denominateur=sum_similarities[user]
    neighbours=user_neighbours[user]
    
    for item in items:
      #faire la prediction
      somme=0
      
      for us in neighbours:
        if item in user_item[us]:
          if (user , us ) in key:
            d=(user , us)
          
            note= user_item[us][item]
            avg=user_avg[us]
            sim= 1- user_matrix[d]
            somme = somme+ sim*(note-avg)

          else :
            d=(us , user)
            note= user_item[us][item]
            avg=user_avg[us]
            sim= 1- user_matrix[d]
            somme = somme+ sim*(note-avg)

      div= somme/denominateur
      pred=moyenne_principale+div

      if pred > 5: 
        pred = 5.0
      if pred < 0 :
        pred= 0.0

      filelist.append((user, item, pred))
      prediction.append(pred)
      reel.append(user_item[user][item])

      cpt+=1
      if cpt % 10000 == 0:
        print(cpt)
        print(div)


      
print(cpt)


#******************************************************************************************

#mae et rmse
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error

print("Mae:     " , mean_absolute_error(reel, prediction))
print("Rmse:    " , mean_squared_error(reel, prediction))





#write the results

o = open("C:\Users\lenovo\Desktop\App_Rec\src\sample\results\results_social_ml.txt",'w')

for i in filelist:
  o.write(str(i[0])+" "+str(i[1])+" "+str(i[2])+"\n")
o.close()