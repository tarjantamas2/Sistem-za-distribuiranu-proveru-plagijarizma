import os
import sys
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def vectorize(Text): return TfidfVectorizer().fit_transform(Text).toarray()
def similarity(doc1, doc2): return cosine_similarity([doc1, doc2])

def compare(candidatePath, referencePaths):
    documentPaths = [candidatePath]
    for i in referencePaths:
        documentPaths.append(i)
    documentPaths = [x for x in filter(lambda path: os.path.isfile(path), documentPaths)]

    documents = [open(_file, encoding='utf-8').read() for _file in documentPaths]
    vectors = vectorize(documents)
    pathsAndVectors = list(zip(documentPaths, vectors))
    results = set()

    for referencePath, referenceVector in pathsAndVectors:
        if referencePath == candidatePath:
            continue
        score = similarity(vectors[0], referenceVector)[0][1]
        result = (referencePath, score)
        results.add(result)

    return results

if __name__ == "__main__":
    candidatePath = sys.argv[1]
    referencePaths = sys.argv[2:]

    for result in compare(candidatePath, referencePaths):
        print(result[0] + "," + str(result[1]))