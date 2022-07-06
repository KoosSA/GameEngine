#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;

out vec2 passTexCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
//uniform vec3 lightPosition;

void main() {

	vec4 worldPosition =  transformationMatrix * vec4(position, 1);

	gl_Position = projectionMatrix * viewMatrix * worldPosition;

	passTexCoord = textureCoord * 150.0;

	surfaceNormal = (transformationMatrix * vec4(normalize(normal), 0.0)).xyz;
	//toLightVector = lightPosition - worldPosition.xyz;
}
