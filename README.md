# QrCPMDecoder

[![](https://jitpack.io/v/Wildanafian/QrCPMDecoder.svg)](https://jitpack.io/#Wildanafian/QrCPMDecoder)

An android library for decode encrypted raw QR CPM String into readable & useable string or model

## Preparation

**Step 1.** Add jitpack in your root build.gradle:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency
```
dependencies {
	        implementation 'com.github.Wildanafian:QrCPMDecoder:latest-release'
	}
```
  
## How to use
There are 2 types of result you can get, result in **model** or just **raw string**
```
  with(QrDecoderHelper()) {
            
            //put your raw QR CPM string here
            decodeQR("raw string")

            //get result in model
            val data = getResult()
            Log.d("##", "In model: " +data.isValid)
            Log.d("##", "In model: " +data.data?.tag85)
            Log.d("##", "In model: " +data.data?.tag50)

            //get result in string
            Log.d("##", "In model: " +getResultInString())
 }
```
