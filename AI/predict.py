import warnings
warnings.filterwarnings("ignore")
import pickle
import sys

with open("AI/growth_model.pkl", "rb") as f:
    model = pickle.load(f)

month = float(sys.argv[1])
result = model.predict([[month]])[0]

print(result)
