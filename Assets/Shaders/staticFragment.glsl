#version 330

in vec2 passTexCoord;

out vec4 outColour;

uniform float useTextures;
uniform vec4 baseColour;
uniform sampler2D tex;

void main() {

	vec4 colour;

	if (useTextures == 1.0) {
		colour = texture(tex, passTexCoord);
	} else {
		colour = baseColour;
	}

	outColour = colour;

}
