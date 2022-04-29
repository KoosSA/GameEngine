#version 460

in vec2 passTexCoord;

out vec4 outColour;

uniform float useTextures;
uniform vec4 baseColour;

uniform sampler2D sampler;

void main() {

	vec4 colour;

	if (useTextures == 1) {
		colour = texture(sampler, passTexCoord);
	} else {
		colour = baseColour;
	}

	outColour = colour;

}
