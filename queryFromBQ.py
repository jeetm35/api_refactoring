from google.cloud import bigquery
import os

#GoogleApplicationCredentials for Sneha
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="C:/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/GoogleApplicationCredentials/SE - GitHub mining-b2fbc7e25e99.json"

def query_github():
	client = bigquery.Client()

	query_repo = client.query("""
		select distinct(t.repo_name) from
		((SELECT repo_name FROM `bigquery-public-data.github_repos.languages`
		CROSS JOIN UNNEST (`bigquery-public-data.github_repos.languages`.language)
		where name = "Java" or name = "java")) as t
		INNER JOIN `bigquery-public-data.github_repos.files` as q
		on t.repo_name = q.repo_name and path = 'pom.xml'
		where t.repo_name in
		(select distinct(m.repo_name)
		from `bigquery-public-data.github_repos.files`as m
		where REGEXP_CONTAINS(m.path,".*/test/.*")) and t.repo_name in (
		SELECT repo_name FROM `bigquery-public-data.github_repos.sample_repos`
		where watch_count>=100 ORDER BY watch_count DESC
		)""")

	res = query_repo.result()
	
	f = open('C:/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/api-refactoring/api_refactoring/repos.txt','w')
	
	for row in res:
		f.write("{}".format(row.repo_name))
		f.write("\n")
	
	f.close()

if __name__ == '__main__':
	query_github()