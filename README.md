# BobScript
Script Language

**пока идёт разработка**

:bug:
текущая версия: __0__     


## ключевые слова
**if**

**while**

**end**

**func**

**return**

## примеры

### Hello, World!
```
print('Hello, world!')
```
### Пример ввода
```
cnt = parseInt(readLine())
i = 0
while i < cnt
  print('*')
  print(i)
end
```

### Факториал
```
func fac(n)
  if n == 0
    return 1
  end
  return fac(n - 1) * n
end

print('Enter the number')
n = parseInt(readLine())
print(fac(n))
```
