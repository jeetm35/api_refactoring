from google.cloud import bigquery
import os

#GoogleApplicationCredentials for Sneha
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="C:/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/GoogleApplicationCredentials/SE - GitHub mining-b2fbc7e25e99.json"

def query_github():
	client = bigquery.Client()
	f = open("query.txt", "r")
	query=f.read()
	print(query)
	query_repo = client.query(query)
	res = query_repo.result()
	
	f = open('C:/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/api-refactoring/api_refactoring/repos.txt','w')
	
	for row in res:
		f.write("{}".format(row.repo_name))
		f.write("\n")
	
	f.close()

if __name__ == '__main__':
	query_github()