import pandas as pd
from sklearn.linear_model import LinearRegression
import pickle

df = pd.read_csv("AI/spending_history.csv", header=None)
df.columns = ["month", "amount"]

growth = []
for i in range(1, len(df)):
    prev = df["amount"][i-1]
    curr = df["amount"][i]
    rate = (curr - prev) / prev
    growth.append(rate)

X = df["month"][1:].values.reshape(-1, 1)
y = growth

model = LinearRegression()
model.fit(X, y)

with open("AI/growth_model.pkl", "wb") as f:
    pickle.dump(model, f)

print("DONE TRAINING MODEL")
