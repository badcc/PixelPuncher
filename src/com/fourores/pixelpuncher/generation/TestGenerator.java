package com.fourores.pixelpuncher.generation;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.fourores.pixelpuncher.model.Block.Material;
import com.fourores.pixelpuncher.model.Chunk;

public class TestGenerator extends Generator {
	public ArrayMap<Long, Chunk> chunks = new ArrayMap<Long, Chunk>();
	public SpriteBatch batch;
	private Random random; // Seed - manual for now.
	private int seed;
	public TestGenerator(SpriteBatch batch) {
		this.batch = batch;
		random = new Random();
		seed = random.nextInt();
	}
	public long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - Integer.MIN_VALUE;
    }
	private int[][] getEmptyCellArray(int chunkX, int chunkY) {
		random.setSeed(toLong(chunkX, chunkY) + seed);
		int[][] cells = new int[16][16];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				boolean on = random.nextFloat() < 0.55f;
				
				cells[x][y] = on ? 1 : 0;
			}
		}
		return cells;
	}
	private int[][] getAutomatonForCells(int[][] cells, ArrayList<Integer> bornList, ArrayList<Integer> surviveList) {
		int[][] newCells = new int[16][16];
		for (int cellRow = 0; cellRow < 16; cellRow++) {
			for (int cellCol = 0; cellCol < 16; cellCol++) {
				boolean condition = false;
				int nbhd = 0;
				/*
				 * fgh
				 * e1d
				 * abc
				 */
				if (cellRow > 0 && cellCol > 0)
					nbhd += cells[cellRow-1][cellCol-1];	// a
				if (cellRow > 0)
					nbhd += cells[cellRow-1][cellCol];		// b
				if (cellRow > 0 && cellCol+1 < 16)
					nbhd += cells[cellRow-1][cellCol+1];	// c
				if (cellCol+1 < 16)
					nbhd += cells[cellRow][cellCol+1];		// d
				if (cellCol > 0)
					nbhd += cells[cellRow][cellCol-1];		// e
				if (cellRow+1 < 16 && cellCol > 0)
					nbhd += cells[cellRow+1][cellCol-1];	// f
				if (cellRow+1 < 16)
					nbhd += cells[cellRow+1][cellCol];		// g
				if (cellRow+1 < 16 && cellCol+1 < 16)
					nbhd += cells[cellRow+1][cellCol+1];	// h
				
				// apply cellular automaton value B... S...
				int currentState = cells[cellRow][cellCol];
//				condition = (Boolean) (currentState == 0 ? (bornList[nbhd] > -1) : (currentState == 1 ? (surviveList[nbhd] > -1) : 0));
				if (currentState == 0)
					condition = bornList.indexOf(nbhd) > -1;
				if (currentState == 1)
					condition = surviveList.indexOf(nbhd) > -1;
					
				newCells[cellRow][cellCol] = condition ? 1 : 0;
			}
		}
		return newCells;
	}
	private int[][] getB678S345678CellAutomaton(int[][] cells) {
		ArrayList<Integer> born = new ArrayList<Integer>();
		born.add(6);
		born.add(7);
		born.add(8);
		
		ArrayList<Integer> survive = new ArrayList<Integer>();
		survive.add(3);
		survive.add(4);
		survive.add(5);
		survive.add(6);
		survive.add(7);
		survive.add(8);
		
		return getAutomatonForCells(cells, born, survive);
	}
	private int[][] getB5678S5678CellAutomaton(int[][] cells) {
		ArrayList<Integer> born = new ArrayList<Integer>();
		born.add(5);
		born.add(6);
		born.add(7);
		born.add(8);
		
		ArrayList<Integer> survive = new ArrayList<Integer>();
		survive.add(5);
		survive.add(6);
		survive.add(7);
		survive.add(8);
		return getAutomatonForCells(cells, born, survive);
	}
	@Override
	public Chunk getChunkAt(int chunkX, int chunkY) {
		int[][] cells = getEmptyCellArray(chunkX, chunkY);

		cells = getB5678S5678CellAutomaton(cells);
		cells = getB678S345678CellAutomaton(cells);
		cells = getB5678S5678CellAutomaton(cells);
		
		long chunkKey = toLong(chunkX, chunkY);
		Chunk chunk = chunks.get(chunkKey);
		if (chunk == null) {
			chunk = new Chunk(batch);
			chunks.put(chunkKey, chunk);
		}
		Vector2 position = new Vector2(chunkX * 16, chunkY * 16);
//		for (int x = 0; x < 16; x++){
//			float pos = (chunkX * 16 + x);
//			float y = (float) (Math.abs(Math.sin(pos / 15) * 10));
//			chunk.setBlockAt((int)pos, (int)y, Material.STONE_MOSSY);
//			chunk.setBlockAt((int)pos, (int)y-1, Material.STONE_CARVED);
//			for (int y2 = 0; y2 < y-2; y2++)
//				chunk.setBlockAt(chunkX * 16 + x, y2, (Math.round(Math.random() * 3) == 0 ? Material.STONE_CRACKED : Material.STONE));
//		}
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				if (cells[x][y] == 1) {
					chunk.setBlockAt((int)position.x + x, (int)position.y + y, (Math.round(Math.random() * 3) == 0 ? Material.STONE_CRACKED : Material.STONE));
				}
			}
		}
		return chunk;
	}
	
//	@Override
//	public Chunk doesChunkExistAt(int x, int y) {
//		Vector2 chunkPos = posToChunkPos(x, y);
//		return null;
//	}
}
