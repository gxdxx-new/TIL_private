# The following untracked working tree files would be overwritten by merge

#### github 저장소의 origin/master 브랜치와 로컬 저장소 master 브랜치가 달라서 생긴 문제이다.

### 해결 방법

```
1. git add -A   // 모든 변경내용을 Staging Area로 넘긴다. 
```

```
2. git stash 	// 현재 상태를 임시 저장한다.
```

```
3. git pull     // github 저장소의 변경사항을 pull
```

```
4. git push     // Staging Area에 있는 내용을 github저장소로 push
```