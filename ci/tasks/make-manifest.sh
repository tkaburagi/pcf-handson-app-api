#!/bin/bash

inputDir=  outputDir=  inputManifest=  artifactId=  packaging=

# optional
hostname=$CF_MANIFEST_HOST # default to env variable from pipeline

while [ $# -gt 0 ]; do
  case $1 in
    -i | --input-dir )
      inputDir=$2
      shift
      ;;
    -o | --output-dir )
      outputDir=$2
      shift
      ;;
    -f | --input-manifest )
      inputManifest=$2
      shift
      ;;
    -a | --artifactId )
      artifactId=$2
      shift
      ;;
    -p | --packaging )
      packaging=$2
      shift
      ;;
    -v | --version-file )
      versionFile=$2
      shift
      ;;
    * )
      echo "Unrecognized option: $1" 1>&2
      exit 1
      ;;
  esac
  shift
done

error_and_exit() {
  echo $1 >&2
  exit 1
}

if [ ! -d "$inputDir" ]; then
  error_and_exit "missing input directory: $inputDir"
fi
if [ ! -d "$outputDir" ]; then
  error_and_exit "missing output directory: $outputDir"
fi
if [ ! -f "$inputManifest" ]; then
  error_and_exit "missing input manifest: $inputManifest"
fi
if [ -z "$artifactId" ]; then
  error_and_exit "missing artifactId!"
fi
if [ -z "$packaging" ]; then
  error_and_exit "missing packaging!"
fi
if [ ! -f "$versionFile" ]; then
  error_and_exit "missing version file: $versionFile"
fi
cp $inputDir/api-tkaburagi-`cat ${versionFile}`.jar $outputDir/api-tkaburagi-`cat ${versionFile}`.jar

outputManifest=$outputDir/manifest-production.yml

cp $inputManifest $outputManifest

# the path in the manifest is always relative to the manifest itself
sed -i -- "s|path: .*$|path: api-tkaburagi-`cat ${versionFile}`.jar|g" $outputManifest

if [ ! -z "$hostname" ]; then
  sed -i "s|host: .*$|host: ${hostname}|g" $outputManifest
fi
cat $outputManifest

pwd
ls -ltr makemanifest
ls -ltrR